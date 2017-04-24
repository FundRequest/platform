package io.fundrequest.core.request;

import io.fundrequest.core.infrastructure.exception.ResourceNotFoundException;
import io.fundrequest.core.infrastructure.mapping.Mappers;
import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.domain.RequestBuilder;
import io.fundrequest.core.request.infrastructure.RequestRepository;
import io.fundrequest.core.request.infrastructure.github.parser.GithubParser;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.request.view.RequestOverviewDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
class RequestServiceImpl implements RequestService {

    private RequestRepository requestRepository;
    private Mappers mappers;
    private GithubParser githubLinkParser;

    public RequestServiceImpl(RequestRepository requestRepository, Mappers mappers, GithubParser githubLinkParser) {
        this.requestRepository = requestRepository;
        this.mappers = mappers;
        this.githubLinkParser = githubLinkParser;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestOverviewDto> findAll() {
        return mappers.mapList(Request.class, RequestOverviewDto.class, requestRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public RequestDto findRequest(Long id) {
        Request request = requestRepository.findOne(id).orElseThrow(ResourceNotFoundException::new);
        return mappers.map(Request.class, RequestDto.class, request);
    }

    @Override
    @Transactional
    public RequestOverviewDto createRequest(String user, CreateRequestCommand command) {
        Optional<Request> request = requestRepository.findByIssueLink(command.getIssueLink());
        request.ifPresent(r -> updateRequestInformation(user, command, r));

        return mappers.map(Request.class, RequestOverviewDto.class,
                request.orElseGet(() -> createNewRequest(user, command))
        );
    }

    private void updateRequestInformation(String user, CreateRequestCommand command, Request r) {
        addWatcherToRequest(user, r);
        addTechnologiesToRequest(command.getTechnologies(), r);
    }

    private void addWatcherToRequest(String user, Request r) {
        r.addWatcher(user);
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
