package io.fundrequest.core.request.domain;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestTest {

    @Test
    public void getTechnologies() {

        Set<RequestTechnology> technologies = new HashSet<>();
        technologies.add(RequestTechnology.builder().technology("python").weight(2L).build());
        technologies.add(RequestTechnology.builder().technology("kotlin").weight(3L).build());
        technologies.add(RequestTechnology.builder().technology("html").weight(4L).build());
        technologies.add(RequestTechnology.builder().technology("css").weight(5L).build());

        Request request = RequestMother.fundRequestArea51().withTechnologies(technologies).build();

        assertThat(request.getTechnologies()).containsExactlyInAnyOrder("python", "kotlin", "html", "css");
    }
}