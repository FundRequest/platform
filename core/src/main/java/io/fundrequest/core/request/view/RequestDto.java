package io.fundrequest.core.request.view;

import io.fundrequest.core.request.domain.RequestStatus;
import io.fundrequest.core.request.domain.RequestType;
import io.fundrequest.core.user.UserDto;
import io.fundrequest.core.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

public class RequestDto {

    private UserService userService;

    private Long id;

    private RequestStatus status;

    private RequestType type;

    private IssueInformationDto issueInformation;

    private Set<String> technologies = new HashSet<>();

    private boolean loggedInUserIsWatcher;

    private Set<String> watchers = new HashSet<>();

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

    public IssueInformationDto getIssueInformation() {
        return issueInformation;
    }

    public void setIssueInformation(IssueInformationDto issueInformation) {
        this.issueInformation = issueInformation;
    }

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public Set<String> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(Set<String> technologies) {
        this.technologies = technologies;
    }

    public boolean isLoggedInUserIsWatcher() {
        return loggedInUserIsWatcher;
    }

    public void setLoggedInUserIsWatcher(boolean loggedInUserIsWatcher) {
        this.loggedInUserIsWatcher = loggedInUserIsWatcher;
    }

    public Set<String> getWatchers() {
        return watchers;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public Set<String> getWatchersNames() {
        Set<String> watchersNames = new HashSet<>();
        for (String watcher : watchers) {
            UserDto user = userService.getUser(watcher);
            if (user != null) {
                watchersNames.add(user.getName());
            }
        }
        return watchersNames;
    }

    public void setWatchers(Set<String> watchers) {
        this.watchers = watchers;
    }
}
