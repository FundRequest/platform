package io.fundrequest.core.request.domain;

public final class RequestMother {

    public static RequestBuilder freeCodeCampNoUserStories() {
        return RequestBuilder
                .aRequest()
                .withIssueInformation(
                        IssueInformationMother.kazuki43zooApiStub().build()
                );
    }
}
