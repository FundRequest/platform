package io.fundrequest.core.request;

import io.fundrequest.core.PrincipalMother;
import io.fundrequest.core.infrastructure.mapping.Mappers;
import io.fundrequest.core.request.claim.SignedClaim;
import io.fundrequest.core.request.claim.UserClaimRequest;
import io.fundrequest.core.request.claim.command.RequestClaimedCommand;
import io.fundrequest.core.request.claim.domain.Claim;
import io.fundrequest.core.request.claim.dto.ClaimDto;
import io.fundrequest.core.request.claim.dto.UserClaimableDto;
import io.fundrequest.core.request.claim.event.RequestClaimedEvent;
import io.fundrequest.core.request.claim.github.GithubClaimResolver;
import io.fundrequest.core.request.claim.infrastructure.ClaimRepository;
import io.fundrequest.core.request.command.CreateRequestCommand;
import io.fundrequest.core.request.domain.IssueInformation;
import io.fundrequest.core.request.domain.IssueInformationMother;
import io.fundrequest.core.request.domain.Platform;
import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.domain.RequestMother;
import io.fundrequest.core.request.domain.RequestStatus;
import io.fundrequest.core.request.domain.RequestType;
import io.fundrequest.core.request.fund.dto.CommentDto;
import io.fundrequest.core.request.infrastructure.RequestRepository;
import io.fundrequest.core.request.infrastructure.github.parser.GithubPlatformIdParser;
import io.fundrequest.core.request.view.ClaimDtoMother;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.request.view.RequestDtoMother;
import io.fundrequest.platform.github.GithubGateway;
import io.fundrequest.platform.github.parser.GithubIssueCommentsResult;
import io.fundrequest.platform.profile.profile.ProfileService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
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

    @Before
    public void setUp() throws Exception {
        requestRepository = mock(RequestRepository.class);
        mappers = mock(Mappers.class);
        githubLinkParser = mock(GithubPlatformIdParser.class);
        profileService = mock(ProfileService.class, RETURNS_DEEP_STUBS);
        githubGateway = mock(GithubGateway.class);
        githubClaimResolver = mock(GithubClaimResolver.class);
        eventPublisher = mock(ApplicationEventPublisher.class);
        claimRepository = mock(ClaimRepository.class);
        requestService = new RequestServiceImpl(
                requestRepository,
                mappers,
                githubLinkParser,
                profileService,
                claimRepository, githubGateway, githubClaimResolver, eventPublisher);
    }

    @Test
    public void findAll() throws Exception {
        List<Request> requests = singletonList(RequestMother.freeCodeCampNoUserStories().build());
        when(requestRepository.findAll()).thenReturn(requests);

        List<RequestDto> expectedRequests = singletonList(RequestDtoMother.freeCodeCampNoUserStories());
        when(mappers.mapList(Request.class, RequestDto.class, requests)).thenReturn(expectedRequests);

        List<RequestDto> result = requestService.findAll();

        assertThat(result).isEqualTo(expectedRequests);
    }

    @Test
    public void findAllByIterable() throws Exception {
        List<Request> requests = singletonList(RequestMother.freeCodeCampNoUserStories().build());
        Set<Long> ids = requests.stream().map(Request::getId).collect(Collectors.toSet());
        when(requestRepository.findAll(ids)).thenReturn(requests);

        List<RequestDto> expectedRequests = singletonList(RequestDtoMother.freeCodeCampNoUserStories());
        when(mappers.mapList(Request.class, RequestDto.class, requests)).thenReturn(expectedRequests);

        List<RequestDto> result = requestService.findAll(ids);

        assertThat(result).isEqualTo(expectedRequests);
    }

    @Test
    public void findRequest() throws Exception {
        Optional<Request> request = Optional.of(RequestMother.freeCodeCampNoUserStories().withWatchers(singletonList("davy")).withId(1L).build());
        when(requestRepository.findOne(request.get().getId())).thenReturn(request);
        RequestDto expectedRequest = RequestDtoMother.freeCodeCampNoUserStories();
        when(mappers.map(Request.class, RequestDto.class, request.get())).thenReturn(expectedRequest);

        RequestDto result = requestService.findRequest(request.get().getId());

        assertThat(result).isEqualTo(expectedRequest);
    }

    @Test
    public void findRequestsForUser() throws Exception {
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
        Principal principal = PrincipalMother.davyvanroy();
        long requestId = 1L;
        Request request = RequestMother.fundRequestArea51().build();
        RequestDto requestDto = RequestDtoMother.fundRequestArea51();
        requestDto.setStatus(RequestStatus.FUNDED);
        when(requestRepository.findOne(requestId)).thenReturn(Optional.of(request));
        when(mappers.map(Request.class, RequestDto.class, request)).thenReturn(requestDto);
        UserClaimableDto expected = UserClaimableDto.builder().claimableByUser(true).claimable(true).build();
        when(githubClaimResolver.userClaimableResult(principal, requestDto))
                .thenReturn(expected);

        UserClaimableDto result = requestService.getUserClaimableResult(principal, requestId);

        assertThat(result).isEqualTo(expected);

        ArgumentCaptor<Request> requestArgumentCaptor = ArgumentCaptor.forClass(Request.class);
        verify(requestRepository).save(requestArgumentCaptor.capture());
        assertThat(requestArgumentCaptor.getValue().getStatus()).isEqualTo(RequestStatus.CLAIMABLE);
    }

    @Test
    public void createWithNewIssue() throws Exception {
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
    public void createWithExistingIssue() throws Exception {
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
    public void addWatcher() throws Exception {
        Principal user = mock(Principal.class);
        when(user.getName()).thenReturn("davy");
        Optional<Request> request = Optional.of(RequestMother.freeCodeCampNoUserStories().withId(1L).build());
        when(requestRepository.findOne(request.get().getId())).thenReturn(request);

        requestService.addWatcherToRequest(user, request.get().getId());

        assertThat(request.get().getWatchers()).contains("davy");
    }

    @Test
    public void removeWatcher() throws Exception {
        Principal user = mock(Principal.class);
        when(user.getName()).thenReturn("davy");
        Optional<Request> request = Optional.of(RequestMother.freeCodeCampNoUserStories().withWatchers(singletonList("davy")).withId(1L).build());
        when(requestRepository.findOne(request.get().getId())).thenReturn(request);

        requestService.removeWatcherFromRequest(user, request.get().getId());

        assertThat(request.get().getWatchers()).isEmpty();
    }

    @Test
    public void signClaimRequest() {
        UserClaimRequest command = UserClaimRequest.builder()
                                                   .platform(Platform.GITHUB)
                                                   .platformId("1")
                                                   .build();
        Principal principal = () -> "davyvanroy";
        Request request = RequestMother.freeCodeCampNoUserStories().build();
        RequestDto requestDto = RequestDtoMother.freeCodeCampNoUserStories();
        when(requestRepository.findByPlatformAndPlatformId(command.getPlatform(), command.getPlatformId())).thenReturn(Optional.of(request));
        when(mappers.map(Request.class, RequestDto.class, request)).thenReturn(requestDto);
        SignedClaim expected = new SignedClaim("", "", Platform.GITHUB, "1", "r", "s", 27);
        when(githubClaimResolver.getSignedClaim(principal, command, requestDto))
                .thenReturn(expected);

        SignedClaim result = requestService.signClaimRequest(principal, command);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void requestClaimed() {
        RequestClaimedCommand command = new RequestClaimedCommand();
        command.setTimestamp(LocalDateTime.now());
        command.setSolver("davyvanroy");
        command.setPlatform(Platform.GITHUB);
        command.setPlatformId("1");
        Request request = RequestMother.freeCodeCampNoUserStories().build();
        RequestDto requestDto = RequestDtoMother.freeCodeCampNoUserStories();
        ClaimDto claimDto = ClaimDtoMother.aClaimDto();
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

    private void verifyClaimEventPublished(RequestClaimedCommand command, RequestDto requestDto, ClaimDto claimDto) {
        ArgumentCaptor<RequestClaimedEvent> captor = ArgumentCaptor.forClass(RequestClaimedEvent.class);
        verify(eventPublisher).publishEvent(captor.capture());
        assertThat(captor.getValue().getSolver()).isEqualTo(command.getSolver());
        assertThat(captor.getValue().getRequestDto()).isEqualTo(requestDto);
        assertThat(captor.getValue().getTimestamp()).isEqualTo(command.getTimestamp());
        assertThat(captor.getValue().getClaimDto()).isEqualTo(claimDto);
    }
}