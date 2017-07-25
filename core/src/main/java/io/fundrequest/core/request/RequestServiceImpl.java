package io.fundrequest.core.request;

import io.fundrequest.core.infrastructure.exception.ResourceNotFoundException;
import io.fundrequest.core.infrastructure.mapping.Mappers;
import io.fundrequest.core.request.command.CreateRequestCommand;
import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.domain.RequestBuilder;
import io.fundrequest.core.request.event.RequestCreatedEvent;
import io.fundrequest.core.request.infrastructure.RequestRepository;
import io.fundrequest.core.request.infrastructure.github.parser.GithubParser;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.request.view.RequestOverviewDto;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
class RequestServiceImpl implements RequestService {

    private RequestRepository requestRepository;
    private Mappers mappers;
    private GithubParser githubLinkParser;
    private ApplicationEventPublisher eventPublisher;

    public RequestServiceImpl(RequestRepository requestRepository, Mappers mappers, GithubParser githubLinkParser, ApplicationEventPublisher eventPublisher) {
        this.requestRepository = requestRepository;
        this.mappers = mappers;
        this.githubLinkParser = githubLinkParser;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestOverviewDto> findAll() {
        return mappers.mapList(Request.class, RequestOverviewDto.class, requestRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestOverviewDto> findRequestsForUser(Principal principal) {
        return mappers.mapList(Request.class, RequestOverviewDto.class, requestRepository.findRequestsForUser(principal.getName()));
    }

    @Override
    @Transactional(readOnly = true)
    public RequestDto findRequest(Long id) {
        Request request = findOne(id);
        return mappers.map(Request.class, RequestDto.class, request);
    }

    @Override
    @Transactional
    public RequestOverviewDto createRequest(Principal principal, CreateRequestCommand command) {
        Optional<Request> request = requestRepository.findByIssueLink(command.getIssueLink());
        String user = principal.getName();
        request.ifPresent(r -> updateRequestInformation(user, command, r));

        Request r = request.orElseGet(() -> createNewRequest(user, command));
        eventPublisher.publishEvent(new RequestCreatedEvent(principal.getName(), r.getIssueInformation().getLink(), r.getIssueInformation().getTitle(), r.getIssueInformation().getSource()));
        return mappers.map(Request.class, RequestOverviewDto.class, r);
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

    private Request findOne(Long requestId) {
        return requestRepository.findOne(requestId).orElseThrow(ResourceNotFoundException::new);
    }

    private void updateRequestInformation(String user, CreateRequestCommand command, Request r) {
        addWatcherToRequest(user, r);
        addTechnologiesToRequest(command.getTechnologies(), r);
    }

    private void addWatcherToRequest(String user, Request r) {
        r.addWatcher(user);
        requestRepository.save(r);
    }

    private void removeWatcherFromRequest(String user, Request r) {
        r.removeWatcher(user);
        requestRepository.save(r);
    }

    private void addTechnologiesToRequest(Set<String> technologies, Request r) {
        technologies.forEach(t ->
                r.addTechnology(t.toLowerCase())
        );
        requestRepository.save(r);
    }

    private Request createNewRequest(String user, CreateRequestCommand command) {
        Request request = RequestBuilder.aRequest()
                .withIssueInformation(githubLinkParser.parseIssue(command.getIssueLink()))
                .withWatchers(Collections.singletonList(user))
                .withTechnologies(command.getTechnologies())
                .build();
        return requestRepository.save(request);
    }
}
