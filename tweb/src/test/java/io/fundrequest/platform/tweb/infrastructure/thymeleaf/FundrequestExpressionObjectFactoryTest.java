package io.fundrequest.platform.tweb.infrastructure.thymeleaf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.thymeleaf.context.IExpressionContext;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class FundrequestExpressionObjectFactoryTest {

    private FundrequestExpressionObjectFactory objectFactory;

    private HashMap<String, Object> expressionObjectsMap;

    @BeforeEach
    void setUp() {
        expressionObjectsMap = new HashMap<>();
        expressionObjectsMap.put("profiles", mock(ProfilesExpressionObject.class));
        expressionObjectsMap.put("funds", mock(FundsExpressionObject.class));
        objectFactory = new FundrequestExpressionObjectFactory(expressionObjectsMap);
    }

    @Test
    void getAllExpressionObjectNames() {
        assertThat(objectFactory.getAllExpressionObjectNames()).containsExactlyInAnyOrder(expressionObjectsMap.keySet().toArray(new String[0]));
    }

    @ParameterizedTest
    @ValueSource(strings = {"profiles, funds"})
    void buildObject(final String expressionObjectName) {
        assertThat(objectFactory.buildObject(mock(IExpressionContext.class), expressionObjectName)).isSameAs(expressionObjectsMap.get(expressionObjectName));
    }

    @ParameterizedTest
    @ValueSource(strings = {"profiles, funds"})
    void isCacheable(final String expressionObjectName) {
        assertThat(objectFactory.isCacheable(expressionObjectName)).isTrue();
    }
}
