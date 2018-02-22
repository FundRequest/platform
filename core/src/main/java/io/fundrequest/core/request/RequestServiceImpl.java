package io.fundrequest.core.request;

import io.fundrequest.core.infrastructure.exception.ResourceNotFoundException;
import io.fundrequest.core.infrastructure.mapping.Mappers;
import io.fundrequest.core.request.claim.CanClaimRequest;
import io.fundrequest.core.request.claim.SignedClaim;
import io.fundrequest.core.request.claim.UserClaimRequest;
import io.fundrequest.core.request.claim.command.RequestClaimedCommand;
import io.fundrequest.core.request.claim.domain.Claim;
import io.fundrequest.core.request.claim.domain.ClaimBuilder;
import io.fundrequest.core.request.claim.dto.ClaimDto;
import io.fundrequest.core.request.claim.event.RequestClaimedEvent;
import io.fundrequest.core.request.claim.github.GithubClaimResolver;
import io.fundrequest.core.request.claim.infrastructure.ClaimRepository;
import io.fundrequest.core.request.command.CreateRequestCommand;
import io.fundrequest.core.request.domain.IssueInformation;
import io.fundrequest.core.request.domain.Platform;
import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.domain.RequestBuilder;
import io.fundrequest.core.request.domain.RequestStatus;
import io.fundrequest.core.request.erc67.ERC67;
import io.fundrequest.core.request.fund.CreateERC67FundRequest;
import io.fundrequest.core.request.fund.FundService;
import io.fundrequest.core.request.fund.command.FundsAddedCommand;
import io.fundrequest.core.request.infrastructure.RequestRepository;
import io.fundrequest.core.request.infrastructure.github.GithubClient;
import io.fundrequest.core.request.infrastructure.github.parser.GithubPlatformIdParser;
import io.fundrequest.core.request.view.RequestDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
class RequestServiceImpl implements RequestService {

    private RequestRepository requestRepository;
    private Mappers mappers;
    private GithubPlatformIdParser githubLinkParser;
    private FundService fundService;
    private ClaimRepository claimRepository;
    private GithubClient githubClient;
    private GithubClaimResolver githubClaimResolver;
    private ApplicationEventPublisher eventPublisher;

    public RequestServiceImpl(RequestRepository requestRepository, Mappers mappers, GithubPlatformIdParser githubLinkParser, FundService fundService, ClaimRepository claimRepository, GithubClient githubClient, GithubClaimResolver githubClaimResolver, ApplicationEventPublisher eventPublisher) {
        this.requestRepository = requestRepository;
        this.mappers = mappers;
        this.githubLinkParser = githubLinkParser;
        this.fundService = fundService;
        this.claimRepository = claimRepository;
        this.githubClient = githubClient;
        this.githubClaimResolver = githubClaimResolver;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> findAll() {
        return mappers.mapList(Request.class, RequestDto.class, requestRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> findAll(Iterable<Long> ids) {
        return mappers.mapList(Request.class, RequestDto.class, requestRepository.findAll(ids));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> findRequestsForUser(Principal principal) {
        return mappers.mapList(Request.class, RequestDto.class, requestRepository.findRequestsForUser(principal.getName()));
    }

    @Override
    @Transactional(readOnly = true)
    public RequestDto findRequest(Long id) {
        Request request = findOne(id);
        return mappers.map(Request.class, RequestDto.class, request);
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
    public RequestDto createRequest(CreateRequestCommand command) {
        Optional<Request> request = requestRepository.findByPlatformAndPlatformId(command.getPlatform(), command.getPlatformId());
        Request r = request.orElseGet(() -> createNewRequest(command));
        fundRequest(command, r);
        return mappers.map(Request.class, RequestDto.class, r);
    }

    @Override
    @Transactional
    public void requestClaimed(RequestClaimedCommand command) {
        Request request = requestRepository.findByPlatformAndPlatformId(command.getPlatform(), command.getPlatformId())
                .orElseThrow(ResourceNotFoundException::new);
        request.setStatus(RequestStatus.CLAIMED);
        request = requestRepository.save(request);
        Claim claim = claimRepository.save(ClaimBuilder.aClaim()
                .withRequestId(request.getId())
                .withSolver(command.getSolver())
                .withTimestamp(command.getTimestamp())
                .withAmountInWei(command.getAmountInWei())
                .build());
        eventPublisher.publishEvent(new RequestClaimedEvent(
                command.getTransactionId(), mappers.map(Request.class, RequestDto.class, request),
                mappers.map(Claim.class, ClaimDto.class, claim),
                command.getSolver(), command.getTimestamp()));
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

    private void fundRequest(CreateRequestCommand command, Request r) {
        FundsAddedCommand fundsCommand = new FundsAddedCommand();
        fundsCommand.setRequestId(r.getId());
        fundsCommand.setAmountInWei(command.getFunds());
        fundsCommand.setTimestamp(command.getTimestamp());
        fundService.addFunds(fundsCommand);
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
                .withParameter("data", createERC67FundRequest.toByteData())
                .build()
                .visualize();
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

    private Set<String> getTechnologies(IssueInformation issueInformation) {
        Map<String, Long> languages = githubClient.getLanguages(issueInformation.getOwner(), issueInformation.getRepo());
        return languages.keySet();
    }
}
