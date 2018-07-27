package io.fundrequest.platform.tweb.infrastructure.thymeleaf;

import org.springframework.stereotype.Component;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.expression.IExpressionObjectFactory;

import java.util.Map;
import java.util.Set;

@Component
public class FundrequestExpressionObjectFactory implements IExpressionObjectFactory {

    private final Map<String, Object> expressionObjectsMap;

    public FundrequestExpressionObjectFactory(final Map<String, Object> expressionObjectsMap) {
        this.expressionObjectsMap = expressionObjectsMap;
    }

    @Override
    public Set<String> getAllExpressionObjectNames() {
        return expressionObjectsMap.keySet();
    }

    @Override
    public Object buildObject(IExpressionContext context, String expressionObjectName) {
        return expressionObjectsMap.get(expressionObjectName);
    }

    @Override
    public boolean isCacheable(String expressionObjectName) {
        return true;
    }
}