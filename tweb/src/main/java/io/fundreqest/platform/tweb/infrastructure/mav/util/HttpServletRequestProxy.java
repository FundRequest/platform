package io.fundreqest.platform.tweb.infrastructure.mav.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class HttpServletRequestProxy {
    private final HttpServletRequest request;

    @Autowired(required = false)
    public HttpServletRequestProxy(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletRequest getRequest() {
        return request;
    }
}
