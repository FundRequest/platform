package io.fundrequest.core.request.view;

import io.fundrequest.core.request.domain.RequestStatus;
import io.fundrequest.core.request.domain.RequestType;

import java.util.Set;

public class RequestOverviewDto {
    private Long id;

    private RequestStatus status;

    private RequestType type;

    private Set<String> watchers;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public Set<String> getWatchers() {
        return watchers;
    }

    public void setWatchers(Set<String> watchers) {
        this.watchers = watchers;
    }
}
