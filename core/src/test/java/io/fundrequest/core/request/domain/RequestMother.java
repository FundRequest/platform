package io.fundrequest.core.request.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class RequestMother {

    public static RequestBuilder freeCodeCampNoUserStories() {
        return RequestBuilder
                .aRequest()
                .withIssueInformation(
                        IssueInformationMother.kazuki43zooApiStub().build()
                                     )
                .withTechnologies(Collections.singleton(RequestTechnology.builder().technology("java").weight(100L).build()))
                .withWatchers(Collections.singletonList("somebody@mailinator.com"));
    }

    public static RequestBuilder fundRequestArea51() {
        Set<RequestTechnology> technologies = new HashSet<>();
        technologies.add(RequestTechnology.builder().technology("python").weight(40L).build());
        technologies.add(RequestTechnology.builder().technology("kotlin").weight(60L).build());
        return RequestBuilder
                .aRequest()
                .withIssueInformation(
                        IssueInformationMother.fundRequestArea51().build()
                                     )
                .withTechnologies(technologies)
                .withWatchers(Collections.singletonList("somebody@mailinator.com"));
    }
}
