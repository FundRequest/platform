package io.fundrequest.restapi.infrastructure;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequestMapping("/api")
public abstract class AbstractRestController {

    protected URI getLocationFromCurrentPath(String path, Object... urlVariables) {
        ServletUriComponentsBuilder builder = ServletUriComponentsBuilder
                .fromCurrentRequest();
        builder.path(path);
        return builder.buildAndExpand(urlVariables).toUri();
    }

}
