package io.fundrequest.core.request.domain;

import java.util.Collections;

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
        return RequestBuilder
                .aRequest()
                .withIssueInformation(
                        IssueInformationMother.fundRequestArea51().build()
                                     )
                .withTechnologies(Collections.singleton("python"))
                .withWatchers(Collections.singletonList("somebody@mailinator.com"));
    }
}
