package io.fundreqest.platform.tweb.fund;

import io.fundrequest.core.request.fund.FundService;
import io.fundrequest.core.request.fund.RefundService;
import io.fundrequest.core.request.fund.UserFundsDto;
import io.fundrequest.core.request.fund.dto.RefundRequestDto;
import io.fundrequest.core.token.dto.TokenValueDto;
import io.fundrequest.core.token.dto.TokenValueDtoMother;
import io.fundrequest.platform.profile.profile.dto.UserProfile;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static io.fundrequest.core.request.fund.domain.RefundRequestStatus.APPROVED;
import static io.fundrequest.core.request.fund.domain.RefundRequestStatus.PENDING;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RefundValidatorTest {

    private static final String FND_TOKEN_ADDRESS = TokenValueDtoMother.FND().build().getTokenAddress();
    private static final String ZRX_TOKEN_ADDRESS = TokenValueDtoMother.ZRX().build().getTokenAddress();

    private RefundValidator refundValidator;

    private RefundService refundService;
    private FundService fundService;

    @BeforeEach
    void setUp() {
        refundService = mock(RefundService.class);
        fundService = mock(FundService.class);
        refundValidator = new RefundValidator(refundService, fundService);
    }

    @Test
    void isRefundable() {
        final String userEtherAddress = "0xeab4";
        final UserFundsDto fund = buildUserFundsDto(userEtherAddress, "5", "", "", "");
        final long requestId = 35L;
        final String requestStatus = "Funded";
        final UserProfile userProfile = UserProfile.builder().etherAddressVerified(true).etherAddress(userEtherAddress).build();
        final TokenValueDto totalFndOnContract = TokenValueDtoMother.FND().totalAmount(TEN).build();

        when(fundService.getFundsFor(requestId, userEtherAddress, totalFndOnContract.getTokenAddress())).thenReturn(Optional.of(totalFndOnContract));
        when(refundService.findAllRefundRequestsFor(requestId, PENDING, APPROVED)).thenReturn(Collections.singletonList(RefundRequestDto.builder().funderAddress("0x4facde56").build()));

        final boolean result = refundValidator.isRefundable(userProfile, fund, requestId, requestStatus);

        assertThat(result).isTrue();
    }

    @Test
    void isRefundable_noProfile() {
        final String userEtherAddress = "0xeab4";
        final UserFundsDto fund = buildUserFundsDto(userEtherAddress, "20", "", "", "");
        final long requestId = 35L;
        final String requestStatus = "Funded";
        final TokenValueDto totalFndOnContract = TokenValueDtoMother.FND().totalAmount(TEN).build();

        when(fundService.getFundsFor(requestId, userEtherAddress, totalFndOnContract.getTokenAddress())).thenReturn(Optional.of(totalFndOnContract));
        when(refundService.findAllRefundRequestsFor(requestId, PENDING, APPROVED)).thenReturn(Collections.singletonList(RefundRequestDto.builder().funderAddress("0x4facde56").build()));

        final boolean result = refundValidator.isRefundable(null, fund, requestId, requestStatus);

        assertThat(result).isFalse();
    }

    @Test
    void isRefundable_userEtherAddressNotVerified() {
        final String userEtherAddress = "0xeab4";
        final UserFundsDto fund = buildUserFundsDto(userEtherAddress, "20", "", "", "");
        final long requestId = 35L;
        final String requestStatus = "Funded";
        final UserProfile userProfile = UserProfile.builder().etherAddressVerified(false).etherAddress(userEtherAddress).build();
        final TokenValueDto totalFndOnContract = TokenValueDtoMother.FND().totalAmount(TEN).build();
        final TokenValueDto totalZrxOnContract = TokenValueDtoMother.ZRX().totalAmount(ZERO).build();

        when(fundService.getFundsFor(requestId, userEtherAddress, totalFndOnContract.getTokenAddress())).thenReturn(Optional.of(totalFndOnContract));
        when(fundService.getFundsFor(requestId, userEtherAddress, totalZrxOnContract.getTokenAddress())).thenReturn(Optional.of(totalZrxOnContract));
        when(refundService.findAllRefundRequestsFor(requestId, PENDING, APPROVED)).thenReturn(Collections.singletonList(RefundRequestDto.builder().funderAddress("0x4facde56").build()));

        final boolean result = refundValidator.isRefundable(userProfile, fund, requestId, requestStatus);

        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @CsvSource( {
                        "20,20,true",
                        "20,0,true",
                        "0,20,true",
                        "20,,true",
                        ",20,true",
                        "0,0,false",
                        "0,,false",
                        ",0,false",
                        ",,false",
                })
    void isRefundable_fundBalanceForAddressOnContractNeedsToBePositive(final String fndFund, final String zrxFund, final String expected) {
        final String userEtherAddress = "0xeab4";
        final UserFundsDto fund = buildUserFundsDto(userEtherAddress, fndFund, zrxFund, "", "");
        final long requestId = 35L;
        final String requestStatus = "Funded";
        final UserProfile userProfile = UserProfile.builder().etherAddressVerified(true).etherAddress(userEtherAddress).build();

        if (StringUtils.isNotBlank(fndFund)) {
            when(fundService.getFundsFor(requestId, userEtherAddress, FND_TOKEN_ADDRESS)).thenReturn(Optional.of(TokenValueDtoMother.FND().totalAmount(new BigDecimal(fndFund)).build()));
        }
        if (StringUtils.isNotBlank(zrxFund)) {
            when(fundService.getFundsFor(requestId, userEtherAddress, ZRX_TOKEN_ADDRESS)).thenReturn(Optional.of(TokenValueDtoMother.ZRX().totalAmount(new BigDecimal(zrxFund)).build()));
        }
        when(refundService.findAllRefundRequestsFor(requestId, PENDING, APPROVED)).thenReturn(Collections.singletonList(RefundRequestDto.builder().funderAddress("0x4facde56").build()));

        final boolean result = refundValidator.isRefundable(userProfile, fund, requestId, requestStatus);

        assertThat(result).isEqualTo(Boolean.valueOf(expected));
    }

    @Test
    void isRefundable_requestStatusNotFunded() {
        final String userEtherAddress = "0xeab4";
        final UserFundsDto fund = buildUserFundsDto(userEtherAddress, "20", "", "", "");
        final long requestId = 35L;
        final String requestStatus = "Claimable";
        final UserProfile userProfile = UserProfile.builder().etherAddressVerified(true).etherAddress(userEtherAddress).build();
        final TokenValueDto totalFndOnContract = TokenValueDtoMother.FND().totalAmount(TEN).build();
        final TokenValueDto totalZrxOnContract = TokenValueDtoMother.ZRX().totalAmount(ZERO).build();

        when(fundService.getFundsFor(requestId, userEtherAddress, totalFndOnContract.getTokenAddress())).thenReturn(Optional.of(totalFndOnContract));
        when(fundService.getFundsFor(requestId, userEtherAddress, totalZrxOnContract.getTokenAddress())).thenReturn(Optional.of(totalZrxOnContract));
        when(refundService.findAllRefundRequestsFor(requestId, PENDING, APPROVED)).thenReturn(Collections.singletonList(RefundRequestDto.builder().funderAddress("0x4facde56").build()));

        final boolean result = refundValidator.isRefundable(userProfile, fund, requestId, requestStatus);

        assertThat(result).isFalse();
    }

    @Test
    void isRefundable_fundAddressNeUserAddress() {
        final String userEtherAddress = "0xeab4";
        final UserFundsDto fund = buildUserFundsDto("0x4657efa", "20", "", "", "");
        final long requestId = 35L;
        final String requestStatus = "Funded";
        final UserProfile userProfile = UserProfile.builder().etherAddressVerified(true).etherAddress(userEtherAddress).build();
        final TokenValueDto totalFndOnContract = TokenValueDtoMother.FND().totalAmount(TEN).build();
        final TokenValueDto totalZrxOnContract = TokenValueDtoMother.ZRX().totalAmount(ZERO).build();

        when(fundService.getFundsFor(requestId, userEtherAddress, totalFndOnContract.getTokenAddress())).thenReturn(Optional.of(totalFndOnContract));
        when(fundService.getFundsFor(requestId, userEtherAddress, totalZrxOnContract.getTokenAddress())).thenReturn(Optional.of(totalZrxOnContract));
        when(refundService.findAllRefundRequestsFor(requestId, PENDING, APPROVED)).thenReturn(Collections.singletonList(RefundRequestDto.builder().funderAddress("0x4facde56").build()));

        final boolean result = refundValidator.isRefundable(userProfile, fund, requestId, requestStatus);

        assertThat(result).isFalse();
    }

    @Test
    void isRefundable_refundRequestAlreadyExists() {
        final String userEtherAddress = "0xeab4";
        final UserFundsDto fund = buildUserFundsDto(userEtherAddress, "20", "", "", "");
        final long requestId = 35L;
        final String requestStatus = "Funded";
        final UserProfile userProfile = UserProfile.builder().etherAddressVerified(true).etherAddress(userEtherAddress).build();
        final TokenValueDto totalFndOnContract = TokenValueDtoMother.FND().totalAmount(TEN).build();
        final TokenValueDto totalZrxOnContract = TokenValueDtoMother.ZRX().totalAmount(ZERO).build();

        when(fundService.getFundsFor(requestId, userEtherAddress, totalFndOnContract.getTokenAddress())).thenReturn(Optional.of(totalFndOnContract));
        when(fundService.getFundsFor(requestId, userEtherAddress, totalZrxOnContract.getTokenAddress())).thenReturn(Optional.of(totalZrxOnContract));
        when(refundService.findAllRefundRequestsFor(requestId, PENDING, APPROVED)).thenReturn(Arrays.asList(RefundRequestDto.builder().funderAddress("0x4facde56").build(),
                                                                                                            RefundRequestDto.builder().funderAddress(userEtherAddress).build()));

        final boolean result = refundValidator.isRefundable(userProfile, fund, requestId, requestStatus);

        assertThat(result).isFalse();
    }

    private UserFundsDto buildUserFundsDto(final String userEtherAddress, final String fndFund, final String zrxFund, final String fndRefund, final String zrxRefund) {
        return UserFundsDto.builder()
                           .funderAddress(userEtherAddress)
                           .fndFunds(StringUtils.isNotBlank(fndFund) ? TokenValueDtoMother.FND().totalAmount(new BigDecimal(fndFund)).build() : null)
                           .otherFunds(StringUtils.isNotBlank(zrxFund) ? TokenValueDtoMother.ZRX().totalAmount(new BigDecimal(zrxFund)).build() : null)
                           .fndRefunds(StringUtils.isNotBlank(fndRefund) ? TokenValueDtoMother.FND().totalAmount(new BigDecimal(fndRefund)).build() : null)
                           .otherRefunds(StringUtils.isNotBlank(zrxRefund) ? TokenValueDtoMother.ZRX().totalAmount(new BigDecimal(zrxRefund)).build() : null)
                           .build();
    }
}
