package io.fundreqest.platform.tweb.infrastructure.thymeleaf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thymeleaf.context.IProcessingContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class FundsDialectTest {

    private FundsDialect fundsDialect;

    private FundsExpressionObject fundsExpressionObject;

    @BeforeEach
    void setUp() {
        fundsExpressionObject = mock(FundsExpressionObject.class);
        fundsDialect = new FundsDialect(fundsExpressionObject);
    }

    @Test
    void getPrefix() {
        assertThat(fundsDialect.getPrefix()).isEqualTo("funds");
    }

    @Test
    void getAdditionalExpressionObjects() {
        assertThat(fundsDialect.getAdditionalExpressionObjects(mock(IProcessingContext.class)).get("funds")).isEqualTo(fundsExpressionObject);
    }
}