package io.fundrequest.platform.tweb.infrastructure.thymeleaf;

import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

@Component
public class FundrequestDialect extends AbstractDialect implements IExpressionObjectDialect {

    private final IExpressionObjectFactory fundrequestExpressionObjectFactory;

    public FundrequestDialect(final IExpressionObjectFactory fundrequestExpressionObjectFactory) {
        super("FundRequest Dialect");
        this.fundrequestExpressionObjectFactory = fundrequestExpressionObjectFactory;
    }

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        return fundrequestExpressionObjectFactory;
    }
}