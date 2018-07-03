package io.fundreqest.platform.tweb.infrastructure.thymeleaf;

import org.thymeleaf.context.IProcessingContext;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionEnhancingDialect;

import java.util.HashMap;
import java.util.Map;

public class FundsDialect extends AbstractDialect implements IExpressionEnhancingDialect {

    private static final String FUNDS_KEY = "funds";

    private final FundsExpressionObject fundsExpressionObject;

    public FundsDialect(final FundsExpressionObject fundsExpressionObject) {
        this.fundsExpressionObject = fundsExpressionObject;
    }

    @Override
    public String getPrefix() {
        return FUNDS_KEY;
    }

    @Override
    public Map<String, Object> getAdditionalExpressionObjects(final IProcessingContext processingContext) {
        final Map<String, Object> expressionObjects = new HashMap<>();
        expressionObjects.put(FUNDS_KEY, fundsExpressionObject);
        return expressionObjects;
    }
}
