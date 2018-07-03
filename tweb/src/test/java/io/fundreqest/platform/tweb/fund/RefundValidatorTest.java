package io.fundreqest.platform.tweb.fund;

import io.fundrequest.core.request.fund.RefundService;
import io.fundrequest.core.request.fund.UserFundsDto;
import io.fundrequest.core.request.fund.dto.RefundRequestDto;
import io.fundrequest.platform.profile.profile.dto.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static io.fundrequest.core.request.fund.domain.RefundRequestStatus.APPROVED;
import static io.fundrequest.core.request.fund.domain.RefundRequestStatus.PENDING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RefundValidatorTest {

    private RefundValidator refundValidator;

    private RefundService refundService;

    @BeforeEach
    void setUp() {
        refundService = mock(RefundService.class);
        refundValidator = new RefundValidator(refundService);
    }

    @Test
    void isRefundable() {
        final UserFundsDto fund = mock(UserFundsDto.class);
        final long requestId = 35L;
        final String requestStatus = "Funded";
        final String userEtherAddress = "0xeab4";
        final UserProfile userProfile = UserProfile.builder().etherAddressVerified(true).etherAddress(userEtherAddress).build();

        when(fund.isRefundable()).thenReturn(true);
        when(fund.getFunderAddress()).thenReturn(userEtherAddress);
        when(refundService.findAllRefundRequestsFor(requestId, PENDING, APPROVED)).thenReturn(Collections.singletonList(RefundRequestDto.builder().funderAddress("0x4facde56").build()));

        final boolean result = refundValidator.isRefundable(userProfile, fund, requestId, requestStatus);

        assertThat(result).isTrue();
    }

    @Test
    void isRefundable_noProfile() {
        final UserFundsDto fund = mock(UserFundsDto.class);
        final long requestId = 35L;
        final String requestStatus = "Funded";
        final String userEtherAddress = "0xeab4";

        when(fund.isRefundable()).thenReturn(true);
        when(fund.getFunderAddress()).thenReturn(userEtherAddress);
        when(refundService.findAllRefundRequestsFor(requestId, PENDING, APPROVED)).thenReturn(Collections.singletonList(RefundRequestDto.builder().funderAddress("0x4facde56").build()));

        final boolean result = refundValidator.isRefundable(null, fund, requestId, requestStatus);

        assertThat(result).isFalse();
    }

    @Test
    void isRefundable_userEtherAddressNotVerified() {
        final UserFundsDto fund = mock(UserFundsDto.class);
        final long requestId = 35L;
        final String requestStatus = "Funded";
        final String userEtherAddress = "0xeab4";
        final UserProfile userProfile = UserProfile.builder().etherAddressVerified(false).etherAddress(userEtherAddress).build();

        when(fund.isRefundable()).thenReturn(true);
        when(fund.getFunderAddress()).thenReturn(userEtherAddress);
        when(refundService.findAllRefundRequestsFor(requestId, PENDING, APPROVED)).thenReturn(Collections.singletonList(RefundRequestDto.builder().funderAddress("0x4facde56").build()));

        final boolean result = refundValidator.isRefundable(userProfile, fund, requestId, requestStatus);

        assertThat(result).isFalse();
    }

    @Test
    void isRefundable_fundIsNotRefundable() {
        final UserFundsDto fund = mock(UserFundsDto.class);
        final long requestId = 35L;
        final String requestStatus = "Funded";
        final String userEtherAddress = "0xeab4";
        final UserProfile userProfile = UserProfile.builder().etherAddressVerified(true).etherAddress(userEtherAddress).build();

        when(fund.isRefundable()).thenReturn(false);
        when(fund.getFunderAddress()).thenReturn(userEtherAddress);
        when(refundService.findAllRefundRequestsFor(requestId, PENDING, APPROVED)).thenReturn(Collections.singletonList(RefundRequestDto.builder().funderAddress("0x4facde56").build()));

        final boolean result = refundValidator.isRefundable(userProfile, fund, requestId, requestStatus);

        assertThat(result).isFalse();
    }

    @Test
    void isRefundable_requestStatusNotFunded() {
        final UserFundsDto fund = mock(UserFundsDto.class);
        final long requestId = 35L;
        final String requestStatus = "Claimable";
        final String userEtherAddress = "0xeab4";
        final UserProfile userProfile = UserProfile.builder().etherAddressVerified(true).etherAddress(userEtherAddress).build();

        when(fund.isRefundable()).thenReturn(true);
        when(fund.getFunderAddress()).thenReturn(userEtherAddress);
        when(refundService.findAllRefundRequestsFor(requestId, PENDING, APPROVED)).thenReturn(Collections.singletonList(RefundRequestDto.builder().funderAddress("0x4facde56").build()));

        final boolean result = refundValidator.isRefundable(userProfile, fund, requestId, requestStatus);

        assertThat(result).isFalse();
    }

    @Test
    void isRefundable_fundAddressNEuserAddress() {
        final UserFundsDto fund = mock(UserFundsDto.class);
        final long requestId = 35L;
        final String requestStatus = "Funded";
        final String userEtherAddress = "0xeab4";
        final UserProfile userProfile = UserProfile.builder().etherAddressVerified(true).etherAddress(userEtherAddress).build();

        when(fund.isRefundable()).thenReturn(true);
        when(fund.getFunderAddress()).thenReturn("0x4657efa");
        when(refundService.findAllRefundRequestsFor(requestId, PENDING, APPROVED)).thenReturn(Collections.singletonList(RefundRequestDto.builder().funderAddress("0x4facde56").build()));

        final boolean result = refundValidator.isRefundable(userProfile, fund, requestId, requestStatus);

        assertThat(result).isFalse();
    }

    @Test
    void isRefundable_refundRequestAlreadyExists() {
        final UserFundsDto fund = mock(UserFundsDto.class);
        final long requestId = 35L;
        final String requestStatus = "Funded";
        final String userEtherAddress = "0xeab4";
        final UserProfile userProfile = UserProfile.builder().etherAddressVerified(true).etherAddress(userEtherAddress).build();

        when(fund.isRefundable()).thenReturn(true);
        when(fund.getFunderAddress()).thenReturn(userEtherAddress);
        when(refundService.findAllRefundRequestsFor(requestId, PENDING, APPROVED)).thenReturn(Arrays.asList(RefundRequestDto.builder().funderAddress("0x4facde56").build(),
                                                                                                            RefundRequestDto.builder().funderAddress(userEtherAddress).build()));

        final boolean result = refundValidator.isRefundable(userProfile, fund, requestId, requestStatus);

        assertThat(result).isFalse();
    }
}