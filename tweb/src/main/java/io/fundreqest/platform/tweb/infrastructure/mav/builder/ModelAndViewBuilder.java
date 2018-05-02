package io.fundreqest.platform.tweb.infrastructure.mav.builder;

import io.fundreqest.platform.tweb.infrastructure.mav.dto.AlertDto;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

public class ModelAndViewBuilder {
    private final ModelAndView modelAndView;

    public ModelAndViewBuilder(Model model) {
        modelAndView = new ModelAndView();
        if (model != null) {
            model.asMap().forEach(this::withObject);
        }
    }

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

    public ModelAndViewBuilder withDangerMessage(String msg) {
        getAlertsFromModel().add(new AlertDto("danger", msg));
        return this;
    }

    private List<AlertDto> getAlertsFromModel() {
        modelAndView.getModel().putIfAbsent("alerts", new ArrayList<AlertDto>());
        return (List<AlertDto>) modelAndView.getModel().get("alerts");
    }


    public ModelAndView build() {
        return modelAndView;
    }
}
