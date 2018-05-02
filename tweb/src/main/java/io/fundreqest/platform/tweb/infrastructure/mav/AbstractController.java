package io.fundreqest.platform.tweb.infrastructure.mav;

import io.fundreqest.platform.tweb.infrastructure.mav.builder.ModelAndViewBuilder;
import io.fundreqest.platform.tweb.infrastructure.mav.builder.RedirectBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractController {
    @Autowired
    private MessageSource messageSource;

    @Autowired(required = false)
    protected HttpServletRequest request;

    public ModelAndViewBuilder modelAndView() {
        return new ModelAndViewBuilder();
    }

    public ModelAndViewBuilder modelAndView(Model model) {
        return new ModelAndViewBuilder(model);
    }


    public RedirectBuilder redirectView(RedirectAttributes redirectAttributes) {
        return new RedirectBuilder(messageSource, redirectAttributes);
    }
}
