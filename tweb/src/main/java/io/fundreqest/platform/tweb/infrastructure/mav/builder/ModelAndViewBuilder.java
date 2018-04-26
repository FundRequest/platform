package io.fundreqest.platform.tweb.infrastructure.mav.builder;

import org.springframework.web.servlet.ModelAndView;

public class ModelAndViewBuilder {
    private final ModelAndView modelAndView;

    public ModelAndViewBuilder() {
        modelAndView = new ModelAndView();
    }

    public ModelAndViewBuilder withView(String view) {
        modelAndView.setViewName(view);
        return this;
    }

    public ModelAndViewBuilder withObject(String name, Object value) {
        modelAndView.addObject(name, value);
        return this;
    }


    public ModelAndView build() {
        return modelAndView;
    }
}
