package io.fundreqest.platform.tweb.infrastructure.mav.builder;

import com.google.common.collect.ArrayListMultimap;
import io.fundreqest.platform.tweb.infrastructure.mav.dto.AlertDto;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RedirectBuilder {

    private String url;
    private RedirectAttributes redirectAttributes;
    private List<AlertDto> alerts = new ArrayList<>();
    private Map<String, Object> attributes;
    private ArrayListMultimap<String, String> requestParams = ArrayListMultimap.create();
    private Map<String, String> pathVariables = new HashMap<>();

    public RedirectBuilder(MessageSource messageSource, RedirectAttributes redirectAttributes) {
        this.redirectAttributes = redirectAttributes;
    }

    public RedirectBuilder url(String url) {
        this.url = url;
        return this;
    }

    private Map<String, Object> getAttributes() {
        if (attributes == null) {
            attributes = new HashMap<>();
        }
        return attributes;
    }

    public RedirectBuilder withDangerMessage(String msg) {
        return addAlert(msg, "danger");
    }

    public RedirectBuilder withSuccessMessage(String msg) {
        return addAlert(msg, "success");

    }

    @NotNull
    private RedirectBuilder addAlert(String msg, String success) {
        alerts.add(new AlertDto(success, msg));
        return this;
    }

    public RedirectBuilder withRedirectAttribute(String key, Object value) {
        redirectAttributes.addFlashAttribute(key, value);
        return this;
    }

    public RedirectBuilder withBindingResult(String commandName, BindingResult result) {
        getAttributes().put(BindingResult.MODEL_KEY_PREFIX + commandName, result);
        return this;
    }

    public RedirectBuilder withRequestParam(String key, String value) {
        requestParams.put(key, value);
        return this;
    }

    public RedirectBuilder withPathVariable(String key, String value) {
        pathVariables.put(key, value);
        return this;
    }

    public ModelAndView build() {
        String requestParamsString = buildRequestParamsString(requestParams);
        RedirectView redirectView = new RedirectView(url + requestParamsString, true);
        if (alerts.size() > 0) {
            redirectAttributes.addFlashAttribute("alerts", alerts);
        }
        if (attributes != null) {
            for (String key : getAttributes().keySet()) {
                redirectAttributes.addFlashAttribute(key, attributes.get(key));
            }
        }
        return new ModelAndView(redirectView, pathVariables);
    }

    private String buildRequestParamsString(ArrayListMultimap<String, String> requestParams) {
        if (!requestParams.isEmpty()) {
            String requestParamsString = "?";
            for (Map.Entry<String, String> entry : requestParams.entries()) {
                requestParamsString = requestParamsString + String.format("%s=%s&", entry.getKey(), entry.getValue());
            }
            return StringUtils.strip(requestParamsString, "&");
        } else {
            return "";
        }
    }
}
