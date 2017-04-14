package io.fundrequest.core.request;

import io.fundrequest.core.infrastructure.mapping.Mappers;
import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.domain.RequestBuilder;
import io.fundrequest.core.request.infrastructure.RequestRepository;
import io.fundrequest.core.request.infrastructure.github.parser.GithubParser;
import io.fundrequest.core.request.view.RequestOverviewDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    @Transactional
    public RequestOverviewDto createRequest(CreateRequestCommand command) {
        Request request = RequestBuilder.aRequest()
                .withIssueInformation(githubLinkParser.parseIssue(command.getIssueLink()))
                .build();
        return mappers.map(Request.class, RequestOverviewDto.class, requestRepository.save(request));
    }
}
