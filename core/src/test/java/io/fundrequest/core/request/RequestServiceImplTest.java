package io.fundrequest.core.request;

import io.fundrequest.core.PrincipalMother;
import io.fundrequest.core.infrastructure.mapping.Mappers;
import io.fundrequest.core.request.claim.SignedClaim;
import io.fundrequest.core.request.claim.UserClaimRequest;
import io.fundrequest.core.request.claim.command.RequestClaimedCommand;
import io.fundrequest.core.request.claim.domain.Claim;
import io.fundrequest.core.request.claim.dto.ClaimDto;
import io.fundrequest.core.request.claim.dto.ClaimableResultDto;
import io.fundrequest.core.request.claim.dto.UserClaimableDto;
import io.fundrequest.core.request.claim.event.RequestClaimedEvent;
import io.fundrequest.core.request.claim.github.GithubClaimResolver;
import io.fundrequest.core.request.claim.infrastructure.ClaimRepository;
import io.fundrequest.core.request.command.CreateRequestCommand;
import io.fundrequest.core.request.command.UpdateRequestStatusCommand;
import io.fundrequest.core.request.domain.IssueInformation;
import io.fundrequest.core.request.domain.IssueInformationMother;
import io.fundrequest.core.request.domain.Platform;
import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.domain.RequestMother;
import io.fundrequest.core.request.domain.RequestStatus;
import io.fundrequest.core.request.domain.RequestType;
import io.fundrequest.core.request.erc67.Erc67Generator;
import io.fundrequest.core.request.fund.domain.CreateERC67FundRequest;
import io.fundrequest.core.request.fund.dto.CommentDto;
import io.fundrequest.core.request.infrastructure.RequestRepository;
import io.fundrequest.core.request.infrastructure.github.parser.GithubPlatformIdParser;
import io.fundrequest.core.request.view.ClaimDtoMother;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.request.view.RequestDtoMother;
import io.fundrequest.platform.github.GithubGateway;
import io.fundrequest.platform.github.parser.GithubIssueCommentsResult;
import io.fundrequest.platform.profile.profile.ProfileService;
import io.fundrequest.platform.profile.profile.dto.UserProfileMother;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static io.fundrequest.core.request.domain.RequestStatus.FUNDED;
import static io.fundrequest.core.request.domain.RequestStatus.OPEN;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RequestServiceImplTest {

    private RequestServiceImpl requestService;
    private RequestRepository requestRepository;
    private Mappers mappers;
    private GithubPlatformIdParser githubLinkParser;
    private ClaimRepository claimRepository;
    private GithubGateway githubGateway;
    private GithubClaimResolver githubClaimResolver;
    private ApplicationEventPublisher eventPublisher;
    private ProfileService profileService;
    private Environment environment;
    private Erc67Generator erc67Generator;

    @Before
    public void setUp() {
        requestRepository = mock(RequestRepository.class);
        mappers = mock(Mappers.class);
        githubLinkParser = mock(GithubPlatformIdParser.class);
        profileService = mock(ProfileService.class, RETURNS_DEEP_STUBS);
        githubGateway = mock(GithubGateway.class);
        githubClaimResolver = mock(GithubClaimResolver.class);
        eventPublisher = mock(ApplicationEventPublisher.class);
        claimRepository = mock(ClaimRepository.class);
        erc67Generator = mock(Erc67Generator.class);
        environment = mock(Environment.class);
        requestService = new RequestServiceImpl(
                requestRepository,
                mappers,
                githubLinkParser,
                profileService,
                claimRepository, githubGateway, githubClaimResolver, eventPublisher, erc67Generator, environment);
    }

    @Test
    public void findAll() {
        List<Request> requests = singletonList(RequestMother.freeCodeCampNoUserStories().build());
        when(requestRepository.findAll()).thenReturn(requests);

        List<RequestDto> expectedRequests = singletonList(RequestDtoMother.freeCodeCampNoUserStories());
        when(mappers.mapList(Request.class, RequestDto.class, requests)).thenReturn(expectedRequests);

        List<RequestDto> result = requestService.findAll();

        assertThat(result).isEqualTo(expectedRequests);
    }

    @Test
    public void generateERC67() {
        when(environment.getProperty("io.fundrequest.payments.erc67.gas", "200000"))
                .thenReturn("150000");

        final CreateERC67FundRequest erc67 = CreateERC67FundRequest
                .builder()
                .amount(new BigDecimal("100.384739"))
                .tokenAddress("0x0000000000000000000000000000000000000000")
                .platform("github")
                .platformId("1")
                .build();

        when(erc67Generator.toByteData(erc67)).thenReturn("0xcae9ca5100000000000000000000000000000000000000000000000000000000deadbeef0000000000000000000000000000000000000000000000056bc75e2d631000000000000000000000000000000000000000000000000000000000000000000060000000000000000000000000000000000000000000000000000000000000000c6769746875627c4141437c310000000000000000000000000000000000000000");

        assertThat(requestService.generateERC67(erc67))
                .isEqualTo(
                        "ethereum:0x0000000000000000000000000000000000000000"
                        + "?value=0"
                        + "&gas=150000"
                        + "&data=0xcae9ca5100000000000000000000000000000000000000000000000000000000deadbeef0000000000000000000000000000000000000000000000056bc75e2d631000000000000000000000000000000000000000000000000000000000000000000060000000000000000000000000000000000000000000000000000000000000000c6769746875627c4141437c310000000000000000000000000000000000000000");
    }

    @Test
    public void findAllByIterable() {
        List<Request> requests = singletonList(RequestMother.freeCodeCampNoUserStories().build());
        Set<Long> ids = requests.stream().map(Request::getId).collect(Collectors.toSet());
        when(requestRepository.findAll(ids)).thenReturn(requests);

        List<RequestDto> expectedRequests = singletonList(RequestDtoMother.freeCodeCampNoUserStories());
        when(mappers.mapList(Request.class, RequestDto.class, requests)).thenReturn(expectedRequests);

        List<RequestDto> result = requestService.findAll(ids);

        assertThat(result).isEqualTo(expectedRequests);
    }

    @Test
    public void findRequest() {
        Optional<Request> request = Optional.of(RequestMother.freeCodeCampNoUserStories().withWatchers(singletonList("davy")).withId(1L).build());
        when(requestRepository.findOne(request.get().getId())).thenReturn(request);
        RequestDto expectedRequest = RequestDtoMother.freeCodeCampNoUserStories();
        when(mappers.map(Request.class, RequestDto.class, request.get())).thenReturn(expectedRequest);

        RequestDto result = requestService.findRequest(request.get().getId());

        assertThat(result).isEqualTo(expectedRequest);
    }

    @Test
    public void findRequestsForUser() {
        Principal user = mock(Principal.class, RETURNS_DEEP_STUBS);

        List<Request> requests = singletonList(RequestMother.freeCodeCampNoUserStories().build());
        when(requestRepository.findRequestsUserIsWatching(user.getName())).thenReturn(requests);

        String address = "0x0";
        when(profileService.getUserProfile(user).getEtherAddress()).thenReturn(address);

        List<Request> fundedRequests = singletonList(RequestMother.fundRequestArea51().build());
        when(requestRepository.findRequestsUserHasFunded(user.getName(), address)).thenReturn(fundedRequests);
        HashSet<Request> userRequets = new HashSet<>();
        userRequets.addAll(requests);
        userRequets.addAll(fundedRequests);
        List<RequestDto> expectedRequests = Arrays.asList(RequestDtoMother.freeCodeCampNoUserStories(), RequestDtoMother.fundRequestArea51());
        when(mappers.mapList(Request.class, RequestDto.class, userRequets)).thenReturn(expectedRequests);


        List<RequestDto> result = requestService.findRequestsForUser(user);

        assertThat(result).isEqualTo(expectedRequests);
    }

    @Test
    public void getUserClaimableResultUpdatesStatus() {
        final Principal principal = PrincipalMother.davyvanroy();
        final long requestId = 1L;
        final Request request = RequestMother.fundRequestArea51().build();
        request.setStatus(FUNDED);
        final IssueInformation issueInformation = request.getIssueInformation();
        final ClaimableResultDto claimableResultDto = ClaimableResultDto.builder().claimable(true).claimableByPlatformUserName("davyvanroy").platform(Platform.GITHUB).build();
        final UserClaimableDto expected = UserClaimableDto.builder().claimableByLoggedInUser(true).claimableByPlatformUserName("davyvanroy").claimable(true).build();
        final ArgumentCaptor<Request> requestArgumentCaptor = ArgumentCaptor.forClass(Request.class);

        when(requestRepository.findOne(requestId)).thenReturn(Optional.of(request));
        when(githubClaimResolver.claimableResult(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber(), FUNDED)).thenReturn(claimableResultDto);
        when(profileService.getUserProfile(principal)).thenReturn(UserProfileMother.davy());

        UserClaimableDto result = requestService.getUserClaimableResult(principal, requestId);

        assertThat(result).isEqualTo(expected);
        verify(requestRepository).save(requestArgumentCaptor.capture());
        assertThat(requestArgumentCaptor.getValue().getStatus()).isEqualTo(RequestStatus.CLAIMABLE);
    }

    @Test
    public void getUserClaimableResultUpdatesStatusToFunded() {
        final Principal principal = PrincipalMother.davyvanroy();
        final long requestId = 1L;
        final Request request = RequestMother.fundRequestArea51().build();
        request.setStatus(RequestStatus.CLAIMABLE);
        final IssueInformation issueInformation = request.getIssueInformation();
        final ClaimableResultDto claimableResultDto = ClaimableResultDto.builder().claimable(false).platform(Platform.GITHUB).build();
        final UserClaimableDto expected = UserClaimableDto.builder().claimable(false).build();
        final ArgumentCaptor<Request> requestArgumentCaptor = ArgumentCaptor.forClass(Request.class);

        when(requestRepository.findOne(requestId)).thenReturn(Optional.of(request));
        when(githubClaimResolver.claimableResult(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber(), RequestStatus.CLAIMABLE)).thenReturn(claimableResultDto);
        when(profileService.getUserProfile(principal)).thenReturn(UserProfileMother.davy());

        UserClaimableDto result = requestService.getUserClaimableResult(principal, requestId);

        assertThat(result).isEqualTo(expected);
        verify(requestRepository).save(requestArgumentCaptor.capture());
        assertThat(requestArgumentCaptor.getValue().getStatus()).isEqualTo(FUNDED);
    }

    @Test
    public void getClaimableResultUpdatesStatus() {
        final long requestId = 1L;
        final Request request = RequestMother.fundRequestArea51().build();
        request.setStatus(FUNDED);
        final IssueInformation issueInformation = request.getIssueInformation();
        final ClaimableResultDto claimableResultDto = ClaimableResultDto.builder().claimable(true).claimableByPlatformUserName("davyvanroy").platform(Platform.GITHUB).build();
        final ArgumentCaptor<Request> requestArgumentCaptor = ArgumentCaptor.forClass(Request.class);

        when(requestRepository.findOne(requestId)).thenReturn(Optional.of(request));
        when(githubClaimResolver.claimableResult(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber(), FUNDED)).thenReturn(claimableResultDto);

        ClaimableResultDto result = requestService.getClaimableResult(requestId);

        assertThat(result).isEqualTo(claimableResultDto);
        verify(requestRepository).save(requestArgumentCaptor.capture());
        assertThat(requestArgumentCaptor.getValue().getStatus()).isEqualTo(RequestStatus.CLAIMABLE);
    }

    @Test
    public void getClaimableResultUpdatesStatusToFunded() {
        final long requestId = 1L;
        final Request request = RequestMother.fundRequestArea51().build();
        request.setStatus(RequestStatus.CLAIMABLE);
        final IssueInformation issueInformation = request.getIssueInformation();
        final ClaimableResultDto claimableResultDto = ClaimableResultDto.builder().claimable(false).platform(Platform.GITHUB).build();
        final ArgumentCaptor<Request> requestArgumentCaptor = ArgumentCaptor.forClass(Request.class);

        when(requestRepository.findOne(requestId)).thenReturn(Optional.of(request));
        when(githubClaimResolver.claimableResult(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber(), RequestStatus.CLAIMABLE)).thenReturn(claimableResultDto);

        ClaimableResultDto result = requestService.getClaimableResult(requestId);

        assertThat(result).isEqualTo(claimableResultDto);
        verify(requestRepository).save(requestArgumentCaptor.capture());
        assertThat(requestArgumentCaptor.getValue().getStatus()).isEqualTo(FUNDED);
    }

    @Test
    public void createWithNewIssue() {
        CreateRequestCommand command = createCommand();
        when(requestRepository.findByPlatformAndPlatformId(command.getPlatform(), command.getPlatformId())).thenReturn(Optional.empty());
        IssueInformation issueInformation = IssueInformationMother.kazuki43zooApiStub().build();
        when(githubLinkParser.parseIssue(command.getPlatformId())).thenReturn(issueInformation);
        when(requestRepository.save(any(Request.class))).then(returnsFirstArg());

        requestService.createRequest(command);

        ArgumentCaptor<Request> savedRequest = ArgumentCaptor.forClass(Request.class);
        verify(requestRepository).save(savedRequest.capture());
        assertThat(savedRequest.getValue().getIssueInformation()).isEqualTo(issueInformation);
        assertThat(savedRequest.getValue().getStatus()).isEqualTo(RequestStatus.OPEN);
        assertThat(savedRequest.getValue().getType()).isEqualTo(RequestType.ISSUE);
    }

    @Test
    public void createWithExistingIssue() {
        CreateRequestCommand command = createCommand();
        Optional<Request> request = Optional.of(RequestMother.freeCodeCampNoUserStories().build());
        when(requestRepository.findByPlatformAndPlatformId(command.getPlatform(), command.getPlatformId())).thenReturn(request);
        IssueInformation issueInformation = IssueInformationMother.kazuki43zooApiStub().build();
        when(githubLinkParser.parseIssue(command.getPlatformId())).thenReturn(issueInformation);
        when(requestRepository.save(any(Request.class))).then(returnsFirstArg());

        requestService.createRequest(command);

    }

    private CreateRequestCommand createCommand() {
        CreateRequestCommand command = new CreateRequestCommand();
        command.setPlatform(Platform.GITHUB);
        command.setPlatformId("1");
        return command;
    }

    @Test
    public void addWatcher() {
        Principal user = mock(Principal.class);
        when(user.getName()).thenReturn("davy");
        Optional<Request> request = Optional.of(RequestMother.freeCodeCampNoUserStories().withId(1L).build());
        when(requestRepository.findOne(request.get().getId())).thenReturn(request);

        requestService.addWatcherToRequest(user, request.get().getId());

        assertThat(request.get().getWatchers()).contains("davy");
    }

    @Test
    public void removeWatcher() {
        Principal user = mock(Principal.class);
        when(user.getName()).thenReturn("davy");
        Optional<Request> request = Optional.of(RequestMother.freeCodeCampNoUserStories().withWatchers(singletonList("davy")).withId(1L).build());
        when(requestRepository.findOne(request.get().getId())).thenReturn(request);

        requestService.removeWatcherFromRequest(user, request.get().getId());

        assertThat(request.get().getWatchers()).isEmpty();
    }

    @Test
    public void signClaimRequest() {
        final UserClaimRequest command = UserClaimRequest.builder()
                                                         .platform(Platform.GITHUB)
                                                         .platformId("1")
                                                         .build();
        final Principal principal = () -> "davyvanroy";
        final Request request = RequestMother.freeCodeCampNoUserStories().build();
        final RequestDto requestDto = RequestDtoMother.freeCodeCampNoUserStories();
        final SignedClaim expected = SignedClaim.builder().solver("").solverAddress("").platform(Platform.GITHUB).platformId("1").r("r").s("s").v(27).build();

        when(requestRepository.findByPlatformAndPlatformId(command.getPlatform(), command.getPlatformId())).thenReturn(Optional.of(request));
        when(mappers.map(Request.class, RequestDto.class, request)).thenReturn(requestDto);
        when(githubClaimResolver.getSignedClaim(principal, command, requestDto)).thenReturn(expected);

        final SignedClaim result = requestService.signClaimRequest(principal, command);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void requestClaimed() {
        final RequestClaimedCommand command = new RequestClaimedCommand();
        command.setTimestamp(LocalDateTime.now());
        command.setSolver("davyvanroy");
        command.setPlatform(Platform.GITHUB);
        command.setPlatformId("1");
        command.setBlockchainEventId(6453L);
        final Request request = RequestMother.freeCodeCampNoUserStories().build();
        final RequestDto requestDto = RequestDtoMother.freeCodeCampNoUserStories();
        final ClaimDto claimDto = ClaimDtoMother.aClaimDto().build();
        when(requestRepository.findByPlatformAndPlatformId(command.getPlatform(), command.getPlatformId())).thenReturn(Optional.of(request));
        when(mappers.map(Request.class, RequestDto.class, request)).thenReturn(requestDto);
        when(mappers.map(eq(Claim.class), eq(ClaimDto.class), any(Claim.class))).thenReturn(claimDto);
        when(requestRepository.save(any(Request.class))).then(returnsFirstArg());
        when(claimRepository.save(any(Claim.class))).then(returnsFirstArg());

        requestService.requestClaimed(command);

        verifyClaimEventPublished(command, requestDto, claimDto);
    }

    @Test
    public void getComments() {
        Request request = RequestMother.fundRequestArea51().build();
        when(requestRepository.findOne(1L)).thenReturn(Optional.of(request));

        List<GithubIssueCommentsResult> githubComments = Collections.singletonList(GithubIssueCommentsResult.builder().title("title").body("#body").build());
        when(githubGateway.getCommentsForIssue(request.getIssueInformation().getOwner(), request.getIssueInformation().getRepo(), request.getIssueInformation().getNumber()))
                .thenReturn(githubComments);

        CommentDto expected = CommentDto.builder().userName("davyvanroy").title("title").body("#body").build();
        when(mappers.mapList(GithubIssueCommentsResult.class, CommentDto.class, githubComments)).thenReturn(Collections.singletonList(expected));

        List<CommentDto> comments = requestService.getComments(1L);

        assertThat(comments.get(0)).isEqualTo(expected);
    }

    @Test
    public void update() {
        final long requestId = 213L;
        final Request request = RequestMother.fundRequestArea51().withStatus(OPEN).build();

        when(requestRepository.findOne(requestId)).thenReturn(Optional.of(request));

        requestService.update(new UpdateRequestStatusCommand(requestId, FUNDED));

        assertThat(request.getStatus()).isEqualTo(FUNDED);
        verify(requestRepository).save(same(request));
    }

    @Test
    public void update_requestNotFound() {
        final long requestId = 213L;

        when(requestRepository.findOne(requestId)).thenReturn(Optional.empty());

        requestService.update(new UpdateRequestStatusCommand(requestId, FUNDED));
    }

    private void verifyClaimEventPublished(final RequestClaimedCommand command, final RequestDto requestDto, final ClaimDto claimDto) {
        final ArgumentCaptor<RequestClaimedEvent> captor = ArgumentCaptor.forClass(RequestClaimedEvent.class);
        verify(eventPublisher).publishEvent(captor.capture());
        assertThat(captor.getValue().getSolver()).isEqualTo(command.getSolver());
        assertThat(captor.getValue().getRequestDto()).isEqualTo(requestDto);
        assertThat(captor.getValue().getTimestamp()).isEqualTo(command.getTimestamp());
        assertThat(captor.getValue().getClaimDto()).isEqualTo(claimDto);
        assertThat(captor.getValue().getBlockchainEventId()).isEqualTo(command.getBlockchainEventId());
    }
}