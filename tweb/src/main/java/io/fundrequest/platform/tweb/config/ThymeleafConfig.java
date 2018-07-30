package io.fundrequest.platform.tweb.config;

import io.fundrequest.platform.tweb.infrastructure.thymeleaf.FundrequestExpressionObjectFactory;
import io.fundrequest.platform.tweb.infrastructure.thymeleaf.FundsExpressionObject;
import io.fundrequest.platform.tweb.infrastructure.thymeleaf.ProfilesExpressionObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.expression.IExpressionObjectFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ThymeleafConfig {

    @Bean
    public IExpressionObjectFactory fundrequestExpressionObjectFactory(final ProfilesExpressionObject profilesExpressionObject, final FundsExpressionObject fundsExpressionObject) {
        final Map<String, Object> expressionObjects = new HashMap<>();
        expressionObjects.put("profiles", profilesExpressionObject);
        expressionObjects.put("funds", fundsExpressionObject);
        return new FundrequestExpressionObjectFactory(expressionObjects);
    }
}