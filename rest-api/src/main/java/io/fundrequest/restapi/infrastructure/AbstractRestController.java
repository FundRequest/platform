package io.fundrequest.restapi.infrastructure;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@CrossOrigin(maxAge = 3600, value = "*")
public abstract class AbstractRestController {

    public static final String PUBLIC_PATH = "/api/public";
    public static final String PRIVATE_PATH = "/api/private";

    protected URI getLocationFromCurrentPath(String path, Object... urlVariables) {
        ServletUriComponentsBuilder builder = ServletUriComponentsBuilder
                .fromCurrentRequest();
        builder.path(path);
        return builder.buildAndExpand(urlVariables).toUri();
    }

    protected URI getPublicLocationFromCurrentPath(String path, Object... urlVariables) {
        ServletUriComponentsBuilder builder = ServletUriComponentsBuilder
                .fromCurrentRequest();
        builder.path(path);
        return URI.create(builder.buildAndExpand(urlVariables).toString().replace(PRIVATE_PATH, PUBLIC_PATH));
    }

    protected URI getPrivateLocationFromCurrentPath(String path, Object... urlVariables) {
        ServletUriComponentsBuilder builder = ServletUriComponentsBuilder
                .fromCurrentRequest();
        builder.path(path);
        return URI.create(builder.buildAndExpand(urlVariables).toString().replace(PUBLIC_PATH, PRIVATE_PATH));
    }

}
