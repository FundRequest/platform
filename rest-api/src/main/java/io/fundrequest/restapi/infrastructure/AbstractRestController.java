package io.fundrequest.restapi.infrastructure;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@CrossOrigin(maxAge = 3600, value = "*")
@RestController
abstract class AbstractRestController {

    protected URI getLocationFromCurrentPath(String path, Object... urlVariables) {
        ServletUriComponentsBuilder builder = ServletUriComponentsBuilder
                .fromCurrentRequest();
        builder.path(path);
        return builder.buildAndExpand(urlVariables).toUri();
    }

}
