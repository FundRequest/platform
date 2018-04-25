package io.fundrequest.core.request.fund;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class EthUtil {
    public static BigDecimal fromWei(BigDecimal rawBalance, int noOfDecimals) {
        final BigDecimal divider = BigDecimal.valueOf(10).pow(noOfDecimals);
        return rawBalance.divide(divider, 6, RoundingMode.HALF_DOWN);
    }

    public static BigDecimal toWei(BigDecimal rawBalance, int noOfDecimals) {
        final BigDecimal multiplier = BigDecimal.valueOf(10).pow(noOfDecimals);
        return rawBalance.multiply(multiplier);
    }
}
