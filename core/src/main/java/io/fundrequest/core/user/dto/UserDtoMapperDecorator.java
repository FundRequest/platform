package io.fundrequest.core.user.dto;

import io.fundrequest.core.user.domain.User;
import io.fundrequest.platform.keycloak.KeycloakRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.stream.Collectors;

public abstract class UserDtoMapperDecorator implements UserDtoMapper {

    @Autowired
    @Qualifier("delegate")
    private UserDtoMapper delegate;

    @Autowired
    private KeycloakRepository keycloakRepository;

    public UserDto map(User user) {
        UserDto result = delegate.map(user);
        if (result != null) {
            result.setUserIdentities(keycloakRepository.getUserIdentities(result.getUserId()).collect(Collectors.toList()));

        }
        return result;
    }

}
