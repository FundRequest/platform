package io.fundrequest.core.request.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GithubLinkValidator.class)
public @interface GithubLink {
    String message() default "Not a valid Github link";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
