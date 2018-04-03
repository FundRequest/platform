package io.fundreqest.platform.tweb.infrastructure.mav;

import io.fundreqest.platform.tweb.infrastructure.mav.builder.ModelAndViewBuilder;
import io.fundreqest.platform.tweb.infrastructure.mav.builder.RedirectBuilder;
import io.fundreqest.platform.tweb.infrastructure.mav.util.HttpServletRequestProxy;
import io.fundreqest.platform.tweb.infrastructure.mav.util.HttpServletResponseProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractController {
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private HttpServletRequestProxy servletRequestProxy;

    @Autowired
    private HttpServletResponseProxy servletResponseProxy;

    protected HttpServletRequest getRequest() {
        return servletRequestProxy.getRequest();
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public ModelAndViewBuilder modelAndView() {
        return new ModelAndViewBuilder(messageSource, getRequest());
    }


    public RedirectBuilder redirectView(RedirectAttributes redirectAttributes) {
        return new RedirectBuilder(messageSource, redirectAttributes);
    }
}
