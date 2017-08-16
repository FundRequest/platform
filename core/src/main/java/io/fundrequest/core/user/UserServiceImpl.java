package io.fundrequest.core.user;

import org.apache.commons.lang.StringUtils;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private RealmResource adminRealmResource;

    @Override
    @Cacheable("users")
    public UserDto getUser(String username) {
        return map(adminRealmResource.users().get(username).toRepresentation());
    }

    private UserDto map(UserRepresentation userRepresentation) {
        UserDto u = new UserDto();
        u.setEmail(userRepresentation.getEmail());
        u.setName(userRepresentation.getFirstName() + " " + userRepresentation.getLastName());
        if (userRepresentation.getAccess().containsKey("picture") &&
                userRepresentation.getAttributes().get("picture").size() > 0
                && StringUtils.isNotBlank(userRepresentation.getAttributes().get("picture").get(0))) {
            u.setPicture(userRepresentation.getAttributes().get("picture").get(0));
        } else if (userRepresentation.getAttributes().containsKey("microsoft_id")) {
            String mId = userRepresentation.getAttributes().get("microsoft_id").get(0);
            u.setPicture("https://apis.live.net/v5.0/" + mId + "/picture?type=small");
        }
        return u;
    }
}
