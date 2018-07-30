package io.fundrequest.core.request;

import io.fundrequest.common.infrastructure.exception.ResourceNotFoundException;
import io.fundrequest.common.infrastructure.mapping.Mappers;
import io.fundrequest.core.request.claim.CanClaimRequest;
import io.fundrequest.core.request.claim.SignedClaim;
import io.fundrequest.core.request.claim.UserClaimRequest;
import io.fundrequest.core.request.claim.command.RequestClaimedCommand;
import io.fundrequest.core.request.claim.domain.Claim;
import io.fundrequest.core.request.claim.domain.ClaimBuilder;
import io.fundrequest.core.request.claim.dto.ClaimDto;
import io.fundrequest.core.request.claim.dto.ClaimableResultDto;
import io.fundrequest.core.request.claim.dto.UserClaimableDto;
import io.fundrequest.core.request.claim.event.RequestClaimableEvent;
import io.fundrequest.core.request.claim.event.RequestClaimedEvent;
import io.fundrequest.core.request.claim.github.GithubClaimResolver;
import io.fundrequest.core.request.claim.infrastructure.ClaimRepository;
import io.fundrequest.core.request.command.CreateRequestCommand;
import io.fundrequest.core.request.command.UpdateRequestStatusCommand;
import io.fundrequest.core.request.domain.IssueInformation;
import io.fundrequest.core.request.domain.Platform;
import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.domain.RequestBuilder;
import io.fundrequest.core.request.domain.RequestStatus;
import io.fundrequest.core.request.domain.RequestTechnology;
import io.fundrequest.core.request.erc67.ERC67;
import io.fundrequest.core.request.erc67.Erc67Generator;
import io.fundrequest.core.request.fund.domain.CreateERC67FundRequest;
import io.fundrequest.core.request.fund.dto.CommentDto;
import io.fundrequest.core.request.infrastructure.RequestRepository;
import io.fundrequest.core.request.infrastructure.RequestSpecification;
import io.fundrequest.core.request.infrastructure.github.parser.GithubPlatformIdParser;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.token.model.TokenValue;
import io.fundrequest.platform.github.GithubGateway;
import io.fundrequest.platform.github.parser.GithubIssueCommentsResult;
import io.fundrequest.platform.profile.profile.ProfileService;
import io.fundrequest.platform.profile.profile.dto.UserProfile;
import io.fundrequest.platform.profile.profile.dto.UserProfileProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
class RequestServiceImpl implements RequestService {

    private RequestRepository requestRepository;
    private Mappers mappers;
    private GithubPlatformIdParser githubLinkParser;
    private ProfileService profileService;
    private ClaimRepository claimRepository;
    private GithubGateway githubGateway;
    private GithubClaimResolver githubClaimResolver;
    private ApplicationEventPublisher eventPublisher;
    private Erc67Generator erc67Generator;
    private Environment environment;

    public RequestServiceImpl(final RequestRepository requestRepository,
                              final Mappers mappers,
                              final GithubPlatformIdParser githubLinkParser,
                              final ProfileService profileService,
                              final ClaimRepository claimRepository,
                              final GithubGateway githubGateway,
                              final GithubClaimResolver githubClaimResolver,
                              final ApplicationEventPublisher eventPublisher,
                              final Erc67Generator erc67Generator,
                              final Environment environment) {
        this.requestRepository = requestRepository;
        this.mappers = mappers;
        this.githubLinkParser = githubLinkParser;
        this.profileService = profileService;
        this.claimRepository = claimRepository;
        this.githubGateway = githubGateway;
        this.githubClaimResolver = githubClaimResolver;
        this.eventPublisher = eventPublisher;
        this.erc67Generator = erc67Generator;
        this.environment = environment;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> findAll() {
        return mappers.mapList(Request.class, RequestDto.class, requestRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<RequestDto> findAllFor(final List<String> projects, final List<String> technologies, final Long lastUpdatedSinceDays) {
        final RequestSpecification specification = new RequestSpecification(projects, technologies, lastUpdatedSinceDays);
        return mappers.mapList(Request.class, RequestDto.class, requestRepository.findAll(specification));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> findAll(Iterable<Long> ids) {
        return mappers.mapList(Request.class, RequestDto.class, requestRepository.findAll(ids));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "technologies", key = "'all'")
    public Set<String> findAllTechnologies() {
        return requestRepository.findAllTechnologies();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "projects", key = "'all'")
    public Set<String> findAllProjects() {
        return requestRepository.findAllProjects();
    }

    @Override
    @Transactional
    public UserClaimableDto getUserClaimableResult(final Principal principal, final Long requestId) {
        final ClaimableResultDto claimableResult = getClaimableResult(requestId);
        return UserClaimableDto.builder()
                               .claimable(claimableResult.isClaimable())
                               .claimableByPlatformUserName(claimableResult.getClaimableByPlatformUserName())
                               .claimableByLoggedInUser(isClaimableByLoggedInUser(principal, claimableResult.getClaimableByPlatformUserName(), claimableResult.getPlatform()))
                               .build();
    }

    @Override
    @Transactional
    public ClaimableResultDto getClaimableResult(final Long requestId) {
        final Request request = findOne(requestId);
        final IssueInformation issueInformation = request.getIssueInformation();
        final ClaimableResultDto claimableResult = githubClaimResolver.claimableResult(issueInformation.getOwner(),
                                                                                       issueInformation.getRepo(),
                                                                                       issueInformation.getNumber(),
                                                                                       request.getStatus());
        checkAndUpdateRequestStatus(request, claimableResult);
        return claimableResult;
    }

    private Boolean isClaimableByLoggedInUser(final Principal principal, final String solver, final Platform platform) {
        return principal == null || solver == null ? false : getUserPlatformUsername(principal, platform).map(u -> u.equalsIgnoreCase(solver)).orElse(false);
    }

    private Optional<String> getUserPlatformUsername(final Principal principal, final Platform platform) {
        if (platform != Platform.GITHUB) {
            throw new RuntimeException("Only github is supported for now");
        }
        return Optional.ofNullable(profileService.getUserProfile(principal))
                       .map(UserProfile::getGithub)
                       .map(UserProfileProvider::getUsername);
    }

    private void checkAndUpdateRequestStatus(final Request request, final ClaimableResultDto claimableResult) {
        if (request.getStatus() == RequestStatus.FUNDED && claimableResult.isClaimable()) {
            final Request updatedRequest = updateStatus(request, RequestStatus.CLAIMABLE);
            eventPublisher.publishEvent(new RequestClaimableEvent(mappers.map(Request.class, RequestDto.class, updatedRequest), LocalDateTime.now()));
        } else if (request.getStatus() == RequestStatus.CLAIMABLE && !claimableResult.isClaimable()) {
            updateStatus(request, RequestStatus.FUNDED);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> findRequestsForUser(Principal principal) {
        Set<Request> result = new HashSet<>();
        String etherAddress = profileService.getUserProfile(principal).getEtherAddress();
        result.addAll(requestRepository.findRequestsUserIsWatching(principal.getName()));
        result.addAll(requestRepository.findRequestsUserHasFunded(principal.getName(), etherAddress));
        return mappers.mapList(Request.class, RequestDto.class, result);
    }

    @Override
    @Transactional(readOnly = true)
    public RequestDto findRequest(Long id) {
        Request request = findOne(id);
        return mappers.map(Request.class, RequestDto.class, request);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getComments(Long requestId) {
        Request request = requestRepository.findOne(requestId).orElseThrow(() -> new EntityNotFoundException("Request not found"));
        return mappers.mapList(GithubIssueCommentsResult.class, CommentDto.class, githubGateway.getCommentsForIssue(request.getIssueInformation().getOwner(),
                                                                                                                    request.getIssueInformation().getRepo(),
                                                                                                                    request.getIssueInformation().getNumber()));
    }

    @Override
    @Transactional(readOnly = true)
    public RequestDto findRequest(Platform platform, String platformId) {
        Request request = requestRepository.findByPlatformAndPlatformId(platform, platformId)
                                           .orElseThrow(ResourceNotFoundException::new);
        return mappers.map(Request.class, RequestDto.class, request);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"projects", "technologies"}, key = "'all'")
    public Long createRequest(CreateRequestCommand command) {
        Optional<Request> request = requestRepository.findByPlatformAndPlatformId(command.getPlatform(), command.getPlatformId());
        Request r = request.orElseGet(() -> createNewRequest(command));
        return r.getId();
    }

    @Override
    @Transactional
    @CacheEvict(value = {"projects", "technologies"}, key = "'all'")
    public Request requestClaimed(RequestClaimedCommand command) {
        final Request request = updateStatus(requestRepository.findByPlatformAndPlatformId(command.getPlatform(), command.getPlatformId())
                                                              .orElseThrow(ResourceNotFoundException::new),
                                             RequestStatus.CLAIMED);
        Claim claim = claimRepository.save(ClaimBuilder.aClaim()
                                                       .withRequestId(request.getId())
                                                       .withSolver(command.getSolver())
                                                       .withTimestamp(command.getTimestamp())
                                                       .withTokenValue(TokenValue.builder()
                                                                                 .tokenAddress(command.getTokenHash())
                                                                                 .amountInWei(command.getAmountInWei())
                                                                                 .build())
                                                       .withBlockchainEventId(command.getBlockchainEventId())
                                                       .build());

        eventPublisher.publishEvent(RequestClaimedEvent.builder()
                                                       .blockchainEventId(command.getBlockchainEventId())
                                                       .requestDto(mappers.map(Request.class, RequestDto.class, request))
                                                       .claimDto(mappers.map(Claim.class, ClaimDto.class, claim))
                                                       .solver(command.getSolver())
                                                       .timestamp(command.getTimestamp())
                                                       .build());
        return request;
    }


    @Override
    @Transactional(readOnly = true)
    public SignedClaim signClaimRequest(Principal user, UserClaimRequest userClaimRequest) {
        return githubClaimResolver.getSignedClaim(user, userClaimRequest, findRequest(userClaimRequest.getPlatform(), userClaimRequest.getPlatformId()));
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean canClaim(Principal user, CanClaimRequest canClaimRequest) {
        return githubClaimResolver.canClaim(user, findRequest(canClaimRequest.getPlatform(), canClaimRequest.getPlatformId()));
    }

    @Override
    @Transactional
    public void addWatcherToRequest(Principal principal, Long requestId) {
        addWatcherToRequest(principal.getName(), findOne(requestId));
    }

    @Override
    @Transactional
    public void removeWatcherFromRequest(Principal principal, Long requestId) {
        removeWatcherFromRequest(principal.getName(), findOne(requestId));
    }

    @Override
    public String generateERC67(final CreateERC67FundRequest createERC67FundRequest) {

        return new ERC67.Builder()
                .withAddress(createERC67FundRequest.getTokenAddress())
                .withNetwork("ethereum")
                .withParameter("value", "0")
                .withParameter("gas", environment.getProperty("io.fundrequest.payments.erc67.gas", "200000"))
                .withParameter("data", erc67Generator.toByteData(createERC67FundRequest))
                .build()
                .visualize();
    }

    @Override
    public void update(final UpdateRequestStatusCommand command) {
        requestRepository.findOne(command.getRequestId()).ifPresent(request -> {
            request.setStatus(command.getNewStatus());
            requestRepository.save(request);
        });
    }

    private Request findOne(Long requestId) {
        return requestRepository.findOne(requestId).orElseThrow(ResourceNotFoundException::new);
    }

    private void addWatcherToRequest(String user, Request r) {
        if (StringUtils.isNotBlank(user)) {
            r.addWatcher(user);
            requestRepository.save(r);
        }
    }

    private void removeWatcherFromRequest(String user, Request r) {
        r.removeWatcher(user);
        requestRepository.save(r);
    }

    private Request createNewRequest(CreateRequestCommand command) {
        IssueInformation issueInformation = githubLinkParser.parseIssue(command.getPlatformId());
        Request request = RequestBuilder.aRequest()
                                        .withIssueInformation(issueInformation)
                                        .withTechnologies(getTechnologies(issueInformation))
                                        .build();
        return requestRepository.save(request);
    }

    private Set<RequestTechnology> getTechnologies(IssueInformation issueInformation) {
        Map<String, Long> languages = githubGateway.getLanguages(issueInformation.getOwner(), issueInformation.getRepo());
        return languages.entrySet()
                        .stream()
                        .map(l -> RequestTechnology.builder().technology(l.getKey()).weight(l.getValue()).build())
                        .sorted(Comparator.comparingLong(RequestTechnology::getWeight).reversed())
                        .limit(4)
                        .collect(Collectors.toSet());
    }

    private Request updateStatus(final Request request, final RequestStatus newStatus) {
        request.setStatus(newStatus);
        return requestRepository.save(request);
    }
}
