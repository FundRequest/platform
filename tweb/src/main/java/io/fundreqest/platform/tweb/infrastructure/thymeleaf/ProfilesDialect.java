package io.fundreqest.platform.tweb.infrastructure.thymeleaf;

import org.thymeleaf.context.IProcessingContext;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionEnhancingDialect;

import java.util.HashMap;
import java.util.Map;

public class ProfilesDialect extends AbstractDialect implements IExpressionEnhancingDialect {

    private static final String PROFILES_KEY = "profiles";
    private final ProfilesExpressionObject profilesExpressionObject;

    public ProfilesDialect(final ProfilesExpressionObject profilesExpressionObject) {
        this.profilesExpressionObject = profilesExpressionObject;
    }

    @Override
    public String getPrefix() {
        return PROFILES_KEY;
    }

    @Override
    public Map<String, Object> getAdditionalExpressionObjects(final IProcessingContext processingContext) {
        final Map<String, Object> expressionObjects = new HashMap<>();
        expressionObjects.put(PROFILES_KEY, profilesExpressionObject);
        return expressionObjects;
    }
}
