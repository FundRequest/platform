package io.fundreqest.platform.tweb.infrastructure.mav.builder;

import org.springframework.context.MessageSource;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

public class ModelAndViewBuilder {
    private final ModelAndView modelAndView;
    private final MessageHolder messageHolder;
    private HttpServletRequest request;

    public ModelAndViewBuilder(MessageSource messageSource, HttpServletRequest request) {
        this.request = request;
        modelAndView = new ModelAndView();
        messageHolder = new MessageHolder(messageSource);
    }

    public ModelAndViewBuilder withView(String view) {
        modelAndView.setViewName(view);
        return this;
    }

    public ModelAndViewBuilder withObject(String name, Object value) {
        modelAndView.addObject(name, value);
        return this;
    }

    public ModelAndViewBuilder withSuccessMessage(String code) {
        messageHolder.addSuccessMessage(code);
        return this;
    }

    public ModelAndViewBuilder withErrorMessage(String code) {
        messageHolder.addErrorMessage(code);
        return this;
    }

    public ModelAndViewBuilder withAlertMessage(String code) {
        messageHolder.addAlertMessage(code);
        return this;
    }

    public ModelAndViewBuilder withInfoMessage(String code) {
        messageHolder.addInfoMessage(code);
        return this;
    }

    public ModelAndViewBuilder withSuccessMessage(String code, String... args) {
        messageHolder.addSuccessMessage(code, args);
        return this;
    }

    public ModelAndViewBuilder withErrorMessage(String code, String... args) {
        messageHolder.addErrorMessage(code, args);
        return this;
    }

    public ModelAndViewBuilder withAlertMessage(String code, String... args) {
        messageHolder.addAlertMessage(code, args);
        return this;
    }

    public ModelAndViewBuilder withInfoMessage(String code, String... args) {
        messageHolder.addInfoMessage(code, args);
        return this;
    }

    public ModelAndView build() {
        if (messageHolder.getMessages() != null) {
            this.request.setAttribute(messageHolder.getMessagesAttributeName(), messageHolder.getMessages());
        }
        return modelAndView;
    }
}
