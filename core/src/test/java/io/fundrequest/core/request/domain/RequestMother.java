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
                .withTechnologies(Collections.singleton("java"))
                .withWatchers(Collections.singletonList("somebody@mailinator.com"));
    }

    public static RequestBuilder fundRequestArea51() {
        Set<String> technologies = new HashSet<>();
        technologies.add("python");
        technologies.add("kotlin");
        return RequestBuilder
                .aRequest()
                .withIssueInformation(
                        IssueInformationMother.fundRequestArea51().build()
                                     )
                .withTechnologies(technologies)
                .withWatchers(Collections.singletonList("somebody@mailinator.com"));
    }
}
