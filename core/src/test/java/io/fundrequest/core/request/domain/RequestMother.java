package io.fundrequest.core.request.domain;

public final class RequestMother {

    public static RequestBuilder freeCodeCampNoUserStories() {
        return RequestBuilder
                .aRequest()
                .withIssueLink("https://github.com/freeCodeCamp/freeCodeCamp/issues/14258")
                .withLabel("Back-end challenges have no user-stories");
    }
}
