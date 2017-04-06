package io.fundrequest.core.infrastructure.repository;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public abstract class AbstractController {
    protected URI getLocationFromCurrentPath(String path, Object... urlVariables) {
        ServletUriComponentsBuilder builder = ServletUriComponentsBuilder
                .fromCurrentRequest();
        builder.path(path);
        return builder.buildAndExpand(urlVariables).toUri();
    }
}
