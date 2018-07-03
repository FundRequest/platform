package io.fundreqest.platform.tweb.infrastructure.thymeleaf;

import io.fundreqest.platform.tweb.fund.RefundValidator;
import io.fundrequest.core.request.fund.UserFundsDto;
import io.fundrequest.platform.profile.profile.dto.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FundsExpressionObjectTest {

    private FundsExpressionObject fundsExpressionObject;

    private RefundValidator refundValidator;

    @BeforeEach
    void setUp() {
        refundValidator = mock(RefundValidator.class);
        fundsExpressionObject = new FundsExpressionObject(refundValidator);
    }

    @ParameterizedTest
    @ValueSource(strings = {"true", "false"})
    public void isRefundable(final String input) {
        final Boolean expected = Boolean.valueOf(input);
        final UserProfile userProfile = mock(UserProfile.class);
        final UserFundsDto fund = mock(UserFundsDto.class);
        final long requestId = 24L;
        final String requestStatus = "Funded";

        when(refundValidator.isRefundable(userProfile, fund, requestId, requestStatus)).thenReturn(expected);

        boolean result = fundsExpressionObject.isRefundable(userProfile, fund, requestId, requestStatus);

        assertThat(result).isEqualTo(expected);
    }
}
