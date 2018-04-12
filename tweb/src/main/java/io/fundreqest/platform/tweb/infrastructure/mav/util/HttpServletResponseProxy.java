package io.fundreqest.platform.tweb.infrastructure.mav.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

@Component
public class HttpServletResponseProxy {
    private final HttpServletResponse response;

    @Autowired(required = false)
    public HttpServletResponseProxy(HttpServletResponse response) {
        this.response = response;
    }

    public HttpServletResponse getResponse() {
        return response;
    }
}
