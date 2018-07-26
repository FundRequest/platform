package io.fundrequest.platform.tweb.infrastructure.thymeleaf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class FundrequestDialectTest {

    private FundrequestDialect fundrequestDialect;

    private FundrequestExpressionObjectFactory fundrequestExpressionObjectFactory;

    @BeforeEach
    void setUp() {
        fundrequestExpressionObjectFactory = mock(FundrequestExpressionObjectFactory.class);
        fundrequestDialect = new FundrequestDialect(fundrequestExpressionObjectFactory);
    }

    @Test
    void getExpressionObjectFactory() {
        assertThat(fundrequestDialect.getExpressionObjectFactory()).isSameAs(fundrequestExpressionObjectFactory);
    }
}