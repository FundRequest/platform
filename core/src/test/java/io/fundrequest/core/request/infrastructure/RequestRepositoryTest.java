package io.fundrequest.core.request.infrastructure;

import io.fundrequest.core.infrastructure.AbstractRepositoryTest;
import io.fundrequest.core.request.domain.FundMother;
import io.fundrequest.core.request.domain.IssueInformation;
import io.fundrequest.core.request.domain.Platform;
import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.domain.RequestMother;
import io.fundrequest.core.request.domain.RequestTechnology;
import io.fundrequest.core.request.fund.domain.Fund;
import io.fundrequest.core.request.fund.infrastructure.FundRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private FundRepository fundRepository;

    @Test
    public void findAll() throws Exception {
        requestRepository.findAll();
    }

    @Test
    public void save() throws Exception {
        Request request = RequestMother
                .freeCodeCampNoUserStories()
                .build();

        requestRepository.saveAndFlush(request);
    }

    @Test
    public void findByPlatformAndPlatformId() throws Exception {
        Request request = RequestMother
                .freeCodeCampNoUserStories()
                .build();
        requestRepository.saveAndFlush(request);

        IssueInformation issueInformation = request.getIssueInformation();
        assertThat(
                requestRepository.findByPlatformAndPlatformId(issueInformation.getPlatform(), issueInformation.getPlatformId())
                  ).isPresent().contains(request);
    }

    @Test
    public void findRequestsForUser() throws Exception {
        Request request = RequestMother
                .freeCodeCampNoUserStories()
                .build();
        requestRepository.saveAndFlush(request);

        String watcher = request.getWatchers().iterator().next();

        assertThat(
                requestRepository.findRequestsUserIsWatching(watcher)
                  ).contains(request);
    }

    @Test
    public void findAllTechnologies() throws Exception {
        Request request = RequestMother.freeCodeCampNoUserStories().build();
        Request request2 = RequestMother.fundRequestArea51().build();

        List<Request> entities = Arrays.asList(request, request2);
        requestRepository.save(entities);
        Set<String> expected = entities.stream().map(Request::getTechnologies).flatMap(Collection::stream)
                                       .collect(Collectors.toSet());

        Set<String> allTechnologies = requestRepository.findAllTechnologies();

        assertThat(allTechnologies)
                .containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    public void findRequestsUserHasFunded() throws Exception {
        Request request = RequestMother
                .freeCodeCampNoUserStories()
                .build();
        request = requestRepository.saveAndFlush(request);
        Fund fund = FundMother.fndFundFunderKnown().requestId(request.getId()).build();
        fundRepository.saveAndFlush(fund);

        List<Request> requests = requestRepository.findRequestsUserHasFunded("wrong", Collections.singletonList(fund.getFunderAddress()));

        assertThat(requests).containsExactly(request);
    }

    @Test
    public void findAllProjects() throws Exception {
        Request request = RequestMother.freeCodeCampNoUserStories().build();
        Request request2 = RequestMother.fundRequestArea51().build();

        List<Request> entities = Arrays.asList(request, request2);
        requestRepository.save(entities);

        Set<String> allTechnologies = requestRepository.findAllProjects();

        assertThat(allTechnologies)
                .containsExactlyInAnyOrder(request.getIssueInformation().getOwner(), request2.getIssueInformation().getOwner());
    }

    @Test
    public void findAllFor() {
        final RequestTechnology java = new RequestTechnology("Java", 1L);
        final RequestTechnology html = new RequestTechnology("HTML", 1L);
        final RequestTechnology css = new RequestTechnology("CSS", 1L);
        final RequestTechnology vue = new RequestTechnology("Vue", 1L);
        final String fundRequest = "FundRequest";
        final String cindercloud = "Cindercloud";
        final String trustWallet = "TrustWallet";
        final Request request1 = buildRequest(fundRequest, 4, java, html);
        final Request request2 = buildRequest(cindercloud, 3, java, html);
        final Request request3 = buildRequest(trustWallet, 2, java, html);
        final Request request4 = buildRequest(cindercloud, 4, html, css, vue);
        final Request request5 = buildRequest(fundRequest, 2, java);
        requestRepository.save(Arrays.asList(request1, request2, request3, request4, request5));
        requestRepository.flush();

        assertThat(findAllFor(Arrays.asList(fundRequest, cindercloud), Arrays.asList(java.getTechnology(), html.getTechnology()), 0L)).containsExactlyInAnyOrder(request1, request2);
        assertThat(findAllFor(Arrays.asList(trustWallet, cindercloud), Arrays.asList(java.getTechnology(), html.getTechnology()), 0L)).containsExactlyInAnyOrder(request3, request2);
        assertThat(findAllFor(new ArrayList<>(), Arrays.asList(java.getTechnology(), html.getTechnology()), 0L)).containsExactlyInAnyOrder(request1, request2, request3);
        assertThat(findAllFor(Arrays.asList(fundRequest, cindercloud), Arrays.asList(java.getTechnology()), 0L)).containsExactlyInAnyOrder(request1, request2, request5);
        assertThat(findAllFor(Arrays.asList(fundRequest, cindercloud), Arrays.asList(java.getTechnology()), 6L)).containsExactlyInAnyOrder(request1, request2, request5);
        assertThat(findAllFor(Arrays.asList(fundRequest, cindercloud), Arrays.asList(java.getTechnology()), 4L)).containsExactlyInAnyOrder(request1, request2, request5);
        assertThat(findAllFor(Arrays.asList(fundRequest, cindercloud), Arrays.asList(java.getTechnology()), 3L)).containsExactlyInAnyOrder(request2, request5);
        assertThat(findAllFor(Arrays.asList(fundRequest, cindercloud), Arrays.asList(html.getTechnology()), 0L)).containsExactlyInAnyOrder(request1, request2, request4);
        assertThat(findAllFor(Arrays.asList(fundRequest, cindercloud), new ArrayList<>(), 0L)).containsExactlyInAnyOrder(request1, request2, request4, request5);
        assertThat(findAllFor(new ArrayList<>(), new ArrayList<>(), 0L)).containsExactlyInAnyOrder(request1, request2, request3, request4, request5);
    }

    private List<Request> findAllFor(List<String> projects, List<String> technologies, long lastUpdatedSinceDays) {
        return requestRepository.findAll(new RequestSpecification(projects, technologies, lastUpdatedSinceDays));
    }

    private Request buildRequest(final String project, final int daysSinceLastUpdate, final RequestTechnology... technologies) {
        return RequestMother.fundRequestArea51()
                            .withIssueInformation(IssueInformation.builder().platform(Platform.GITHUB).platformId(UUID.randomUUID().toString()).owner(project).build())
                            .withTechnologies(new HashSet<>(Arrays.asList(technologies)))
                            .withLastModifiedDate(LocalDateTime.now().minusDays(daysSinceLastUpdate))
                            .build();
    }
}