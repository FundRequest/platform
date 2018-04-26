package io.fundreqest.platform.tweb.infrastructure.mav;

import io.fundreqest.platform.tweb.infrastructure.mav.builder.ModelAndViewBuilder;
import io.fundreqest.platform.tweb.infrastructure.mav.builder.RedirectBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public abstract class AbstractController {
    @Autowired
    private MessageSource messageSource;

    public ModelAndViewBuilder modelAndView() {
        return new ModelAndViewBuilder();
    }


    public RedirectBuilder redirectView(RedirectAttributes redirectAttributes) {
        return new RedirectBuilder(messageSource, redirectAttributes);
    }
}
