package io.fundrequest.core.request.infrastructure;

import io.fundrequest.core.infrastructure.AbstractRepositoryTest;
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
    public void findByLink() throws Exception {
        Request request = RequestMother
                .freeCodeCampNoUserStories()
                .build();
        requestRepository.saveAndFlush(request);

        assertThat(
                requestRepository.findByIssueLink(request.getIssueInformation().getLink())
        ).isPresent().contains(request);
    }
}