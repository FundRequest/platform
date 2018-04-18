package io.fundrequest.core.request.validation;


import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class GithubLinkValidator implements ConstraintValidator<GithubLink, String> {


    private final String regex;

    public GithubLinkValidator() {
        regex = "^https:\\/\\/github\\.com\\/FundRequest\\/.+\\/issues\\/\\d+$";
    }

    public void initialize(GithubLink constraint) {
    }

    public boolean isValid(String link, ConstraintValidatorContext context) {
        return StringUtils.isEmpty(link)
               || link.matches(regex);
    }

}
