package io.fundreqest.platform.tweb.infrastructure.mav.builder;

import com.google.common.collect.ArrayListMultimap;
import io.fundreqest.platform.tweb.infrastructure.mav.messaging.Messages;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;

public class RedirectBuilder {

    private String url;
    private RedirectAttributes redirectAttributes;
    private MessageHolder messageHolder;
    private Map<String, Object> attributes;
    private ArrayListMultimap<String, String> requestParams = ArrayListMultimap.create();
    private Map<String, String> pathVariables = new HashMap<>();

    public RedirectBuilder(MessageSource messageSource, RedirectAttributes redirectAttributes) {
        this.redirectAttributes = redirectAttributes;
        messageHolder = new MessageHolder(messageSource);
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

    public RedirectBuilder withSuccessMessage(String code) {
        messageHolder.addSuccessMessage(code);
        return this;
    }

    public RedirectBuilder withErrorMessage(String code) {
        messageHolder.addErrorMessage(code);
        return this;
    }

    public RedirectBuilder withAlertMessage(String code) {
        messageHolder.addAlertMessage(code);
        return this;
    }

    public RedirectBuilder withInfoMessage(String code) {
        messageHolder.addInfoMessage(code);
        return this;
    }

    public RedirectBuilder withSuccessMessage(String code, String... args) {
        messageHolder.addSuccessMessage(code, args);
        return this;
    }

    public RedirectBuilder withErrorMessage(String code, String... args) {
        messageHolder.addErrorMessage(code, args);
        return this;
    }

    public RedirectBuilder withAlertMessage(String code, String... args) {
        messageHolder.addAlertMessage(code, args);
        return this;
    }

    public RedirectBuilder withInfoMessage(String code, String... args) {
        messageHolder.addInfoMessage(code, args);
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

    public RedirectBuilder withMessages(Messages messages) {
        messageHolder.addMessages(messages);
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
        if (messageHolder.getMessages() != null) {
            redirectAttributes.addFlashAttribute(messageHolder.getMessagesAttributeName(), messageHolder.getMessages());
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
