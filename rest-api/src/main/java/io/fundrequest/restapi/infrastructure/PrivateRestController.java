package io.fundrequest.restapi.infrastructure;

import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(PrivateRestController.PRIVATE_API_LOCATION)
public abstract class PrivateRestController extends AbstractRestController {
    public static final String PRIVATE_API_LOCATION = "/api/private";
}
