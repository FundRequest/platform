package io.fundrequest.core.request.infrastructure;

import io.fundrequest.core.infrastructure.AbstractRepositoryTest;
import io.fundrequest.core.request.domain.IssueInformation;
import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.domain.RequestMother;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private RequestRepository requestRepository;

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
                requestRepository.findRequestsForUser(watcher)
        ).contains(request);
    }
}