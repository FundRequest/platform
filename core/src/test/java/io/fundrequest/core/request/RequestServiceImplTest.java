package io.fundrequest.core.request;

import io.fundrequest.core.infrastructure.mapping.Mappers;
import io.fundrequest.core.request.command.CreateRequestCommand;
import io.fundrequest.core.request.domain.IssueInformation;
import io.fundrequest.core.request.domain.IssueInformationMother;
import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.domain.RequestMother;
import io.fundrequest.core.request.domain.RequestStatus;
import io.fundrequest.core.request.domain.RequestType;
import io.fundrequest.core.request.infrastructure.RequestRepository;
import io.fundrequest.core.request.infrastructure.github.parser.GithubParser;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.request.view.RequestDtoMother;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RequestServiceImplTest {

    private RequestServiceImpl requestService;
    private RequestRepository requestRepository;
    private Mappers mappers;
    private GithubParser githubLinkParser;
    private ApplicationEventPublisher eventPublisher;

    @Before
    public void setUp() throws Exception {
        requestRepository = mock(RequestRepository.class);
        mappers = mock(Mappers.class);
        githubLinkParser = mock(GithubParser.class);
        eventPublisher = mock(ApplicationEventPublisher.class);
        requestService = new RequestServiceImpl(
                requestRepository,
                mappers,
                githubLinkParser,
                eventPublisher
        );
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
        when(requestRepository.findRequestsForUser(user.getName())).thenReturn(requests);

        List<RequestDto> expectedRequests = singletonList(RequestDtoMother.freeCodeCampNoUserStories());
        when(mappers.mapList(Request.class, RequestDto.class, requests)).thenReturn(expectedRequests);

        List<RequestDto> result = requestService.findRequestsForUser(user);

        assertThat(result).isEqualTo(expectedRequests);
    }

    @Test
    public void createWithNewIssue() throws Exception {
        CreateRequestCommand command = createCommand();
        when(requestRepository.findByIssueLink(command.getIssueLink())).thenReturn(Optional.empty());
        IssueInformation issueInformation = IssueInformationMother.kazuki43zooApiStub().build();
        when(githubLinkParser.parseIssue(command.getIssueLink())).thenReturn(issueInformation);
        when(requestRepository.save(any(Request.class))).then(returnsFirstArg());

        requestService.createRequest(mock(Principal.class, RETURNS_DEEP_STUBS), command);

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
        when(requestRepository.findByIssueLink(command.getIssueLink())).thenReturn(request);
        IssueInformation issueInformation = IssueInformationMother.kazuki43zooApiStub().build();
        when(githubLinkParser.parseIssue(command.getIssueLink())).thenReturn(issueInformation);
        when(requestRepository.save(any(Request.class))).then(returnsFirstArg());

        Principal user = mock(Principal.class, RETURNS_DEEP_STUBS);
        when(user.getName()).thenReturn("davy");

        requestService.createRequest(user, command);

        assertThat(request.get().getWatchers()).contains("davy");
    }

    private CreateRequestCommand createCommand() {
        CreateRequestCommand command = new CreateRequestCommand();
        command.setIssueLink("link");
        HashSet<String> technologies = new HashSet<>();
        technologies.add("java");
        command.setTechnologies(technologies);
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
}