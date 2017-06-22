package io.fundrequest.core.request.event;

import io.fundrequest.core.request.domain.RequestSource;

public class RequestCreatedEvent {

    private String creator;
    private String link;

    private String title;

    private RequestSource source;

    public RequestCreatedEvent(String creator, String link, String title, RequestSource source) {
        this.creator = creator;
        this.link = link;
        this.title = title;
        this.source = source;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public RequestSource getSource() {
        return source;
    }

    public String getCreator() {
        return creator;
    }
}
