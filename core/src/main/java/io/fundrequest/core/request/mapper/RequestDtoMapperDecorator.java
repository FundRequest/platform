package io.fundrequest.core.request.mapper;

import io.fundrequest.core.infrastructure.SecurityContextService;
import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.request.view.RequestDtoMapper;
import io.fundrequest.core.user.UserService;
import io.fundrequest.core.user.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;

import java.util.Objects;
import java.util.stream.Collectors;

public abstract class RequestDtoMapperDecorator implements RequestDtoMapper {

    @Autowired
    @Qualifier("delegate")
    private RequestDtoMapper delegate;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityContextService securityContextService;

    public RequestDto map(Request request) {
        RequestDto result = delegate.map(request);
        Authentication currentAuth = securityContextService.getLoggedInUser();
        if (result != null && currentAuth != null) {
            result.setLoggedInUserIsWatcher(request.getWatchers().contains(currentAuth.getName()));
            result.setWatchers(request.getWatchers().stream().map(this::getUser).filter(Objects::nonNull).collect(Collectors.toSet()));
        }
        return result;
    }

    private String getUser(String x) {
        UserDto user = userService.getUser(x);
        return user == null ? null : user.getEmail();
    }
}
