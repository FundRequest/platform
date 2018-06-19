package io.fundrequest.core.request.fund.dto;

import io.fundrequest.core.infrastructure.SecurityContextService;
import io.fundrequest.core.request.fund.domain.Refund;
import io.fundrequest.core.token.dto.TokenValueDto;
import io.fundrequest.core.token.mapper.TokenValueDtoMapper;
import io.fundrequest.core.token.model.TokenValue;
import io.fundrequest.platform.profile.profile.ProfileService;
import io.fundrequest.platform.profile.profile.dto.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RefundToFundWithUserDtoMapperTest {

    private RefundToFundWithUserDtoMapper mapper;

    private ProfileService profileService;
    private TokenValueDtoMapper tokenValueDtoMapper;
    private SecurityContextService securityContextService;

    @BeforeEach
    void setUp() {
        profileService = mock(ProfileService.class);
        tokenValueDtoMapper = mock(TokenValueDtoMapper.class);
        securityContextService = mock(SecurityContextService.class);
        mapper = new RefundToFundWithUserDtoMapper(profileService, tokenValueDtoMapper, securityContextService);
    }

    @Test
    void map_otherToken() {
        final String refunderUserId = "gdhfgj";
        final String refunderAddress = "0x45HDF80";
        final String refunderUserName = "hgjgfk";
        final TokenValue tokenValue = mock(TokenValue.class);
        final Refund refund = Refund.builder()
                              .requestedBy(refunderUserId)
                              .funderAddress(refunderAddress)
                              .tokenValue(tokenValue)
                              .requestId(543L)
                              .creationDate(LocalDateTime.now()).blockchainEventId(238L)
                              .build();
        final TokenValueDto tokenValueDto = TokenValueDto.builder().tokenSymbol("SOT").totalAmount(new BigDecimal("40")).build();
        final UserProfile refundUserProfile = UserProfile.builder().id(refunderUserId).etherAddress(refunderAddress).name(refunderUserName).build();
        final UserProfile loggedInUserProfile = UserProfile.builder().id(refunderUserId).etherAddress(refunderAddress).name(refunderUserName).build();


        when(tokenValueDtoMapper.map(tokenValue)).thenReturn(tokenValueDto);
        when(profileService.getUserProfile(refunderUserId)).thenReturn(refundUserProfile);
        when(securityContextService.getLoggedInUserProfile()).thenReturn(Optional.of(loggedInUserProfile));

        final FundWithUserDto result = mapper.map(refund);

        assertThat(result.getFunder()).isEqualTo(refunderUserName);
        assertThat(result.getFunderAddress()).isEqualTo(refunderAddress);
        assertThat(result.getFndFunds()).isNull();
        assertThat(result.getOtherFunds().getTokenSymbol()).isEqualTo(tokenValueDto.getTokenSymbol());
        assertThat(result.getOtherFunds().getTokenAddress()).isEqualTo(tokenValueDto.getTokenAddress());
        assertThat(result.getOtherFunds().getTotalAmount()).isEqualTo(tokenValueDto.getTotalAmount().negate());
        assertThat(result.isLoggedInUser()).isTrue();
        assertThat(result.getTimestamp()).isEqualToIgnoringMinutes(LocalDateTime.now());
        assertThat(result.isRefund()).isTrue();
    }

    @Test
    void map_FND() {
        final String refunderUserId = "gdhfgj";
        final String refunderAddress = "0x45HDF80";
        final String refunderUserName = "hgjgfk";
        final TokenValue tokenValue = mock(TokenValue.class);
        final Refund refund = Refund.builder()
                              .requestedBy(refunderUserId)
                              .funderAddress(refunderAddress)
                              .tokenValue(tokenValue)
                              .requestId(543L)
                              .creationDate(LocalDateTime.now()).blockchainEventId(238L)
                              .build();
        final TokenValueDto tokenValueDto = TokenValueDto.builder().tokenSymbol("FND").totalAmount(new BigDecimal("445")).build();
        final UserProfile refundUserProfile = UserProfile.builder().id(refunderUserId).etherAddress(refunderAddress).name(refunderUserName).build();
        final UserProfile loggedInUserProfile = UserProfile.builder().id(refunderUserId).etherAddress(refunderAddress).name(refunderUserName).build();


        when(tokenValueDtoMapper.map(tokenValue)).thenReturn(tokenValueDto);
        when(profileService.getUserProfile(refunderUserId)).thenReturn(refundUserProfile);
        when(securityContextService.getLoggedInUserProfile()).thenReturn(Optional.of(loggedInUserProfile));

        final FundWithUserDto result = mapper.map(refund);

        assertThat(result.getFunder()).isEqualTo(refunderUserName);
        assertThat(result.getFunderAddress()).isEqualTo(refunderAddress);
        assertThat(result.getFndFunds().getTokenSymbol()).isEqualTo(tokenValueDto.getTokenSymbol());
        assertThat(result.getFndFunds().getTokenAddress()).isEqualTo(tokenValueDto.getTokenAddress());
        assertThat(result.getFndFunds().getTotalAmount()).isEqualTo(tokenValueDto.getTotalAmount().negate());
        assertThat(result.getOtherFunds()).isNull();
        assertThat(result.isLoggedInUser()).isTrue();
        assertThat(result.getTimestamp()).isEqualToIgnoringMinutes(LocalDateTime.now());
        assertThat(result.isRefund()).isTrue();
    }

    @Test
    void map_loggedInUserDifferentUser() {
        final String refunderUserId = "gdhfgj";
        final String refunderAddress = "0x45HDF80";
        final String refunderUserName = "hgjgfk";
        final TokenValue tokenValue = mock(TokenValue.class);
        final Refund refund = Refund.builder()
                              .requestedBy(refunderUserId)
                              .funderAddress(refunderAddress)
                              .tokenValue(tokenValue)
                              .requestId(543L)
                              .creationDate(LocalDateTime.now()).blockchainEventId(238L)
                              .build();
        final TokenValueDto tokenValueDto = TokenValueDto.builder().tokenSymbol("SOT").totalAmount(new BigDecimal("23")).build();
        final UserProfile refundUserProfile = UserProfile.builder().id(refunderUserId).etherAddress(refunderAddress).name(refunderUserName).build();
        final UserProfile loggedInUserProfile = UserProfile.builder().id("fgh").etherAddress("0xrdtdr").name(refunderUserName).build();


        when(tokenValueDtoMapper.map(tokenValue)).thenReturn(tokenValueDto);
        when(profileService.getUserProfile(refunderUserId)).thenReturn(refundUserProfile);
        when(securityContextService.getLoggedInUserProfile()).thenReturn(Optional.of(loggedInUserProfile));

        final FundWithUserDto result = mapper.map(refund);

        assertThat(result.getFunder()).isEqualTo(refunderUserName);
        assertThat(result.getFunderAddress()).isEqualTo(refunderAddress);
        assertThat(result.getFndFunds()).isNull();
        assertThat(result.getOtherFunds().getTokenSymbol()).isEqualTo(tokenValueDto.getTokenSymbol());
        assertThat(result.getOtherFunds().getTokenAddress()).isEqualTo(tokenValueDto.getTokenAddress());
        assertThat(result.getOtherFunds().getTotalAmount()).isEqualTo(tokenValueDto.getTotalAmount().negate());
        assertThat(result.isLoggedInUser()).isFalse();
        assertThat(result.getTimestamp()).isEqualToIgnoringMinutes(LocalDateTime.now());
        assertThat(result.isRefund()).isTrue();
    }

    @Test
    void map_loggedInUserDifferentAddress() {
        final String refunderUserId = "gdhfgj";
        final String refunderAddress = "0x45HDF80";
        final String refunderUserName = "hgjgfk";
        final TokenValue tokenValue = mock(TokenValue.class);
        final Refund refund = Refund.builder()
                              .requestedBy(refunderUserId)
                              .funderAddress(refunderAddress)
                              .tokenValue(tokenValue)
                              .requestId(543L)
                              .creationDate(LocalDateTime.now()).blockchainEventId(238L)
                              .build();
        final TokenValueDto tokenValueDto = TokenValueDto.builder().tokenSymbol("SOT").totalAmount(new BigDecimal("23")).build();
        final UserProfile refundUserProfile = UserProfile.builder().id(refunderUserId).etherAddress(refunderAddress).name(refunderUserName).build();
        final UserProfile loggedInUserProfile = UserProfile.builder().id(refunderUserId).etherAddress("0xrdtdr").name(refunderUserName).build();


        when(tokenValueDtoMapper.map(tokenValue)).thenReturn(tokenValueDto);
        when(profileService.getUserProfile(refunderUserId)).thenReturn(refundUserProfile);
        when(securityContextService.getLoggedInUserProfile()).thenReturn(Optional.of(loggedInUserProfile));

        final FundWithUserDto result = mapper.map(refund);

        assertThat(result.getFunder()).isEqualTo(refunderUserName);
        assertThat(result.getFunderAddress()).isEqualTo(refunderAddress);
        assertThat(result.getFndFunds()).isNull();
        assertThat(result.getOtherFunds().getTokenSymbol()).isEqualTo(tokenValueDto.getTokenSymbol());
        assertThat(result.getOtherFunds().getTokenAddress()).isEqualTo(tokenValueDto.getTokenAddress());
        assertThat(result.getOtherFunds().getTotalAmount()).isEqualTo(tokenValueDto.getTotalAmount().negate());
        assertThat(result.isLoggedInUser()).isFalse();
        assertThat(result.getTimestamp()).isEqualToIgnoringMinutes(LocalDateTime.now());
        assertThat(result.isRefund()).isTrue();
    }

    @Test
    void map_noRefunderUserId() {
        final String refunderAddress = "0x45HDF80";
        final TokenValue tokenValue = mock(TokenValue.class);
        final Refund refund = Refund.builder()
                                    .funderAddress(refunderAddress)
                                    .tokenValue(tokenValue)
                                    .requestId(543L)
                                    .creationDate(LocalDateTime.now()).blockchainEventId(238L)
                                    .build();
        final TokenValueDto tokenValueDto = TokenValueDto.builder().tokenSymbol("SOT").totalAmount(new BigDecimal("23")).build();
        final UserProfile loggedInUserProfile = UserProfile.builder().id("efsgfbg").etherAddress("0xrdtdr").name("sfas").build();


        when(tokenValueDtoMapper.map(tokenValue)).thenReturn(tokenValueDto);
        when(securityContextService.getLoggedInUserProfile()).thenReturn(Optional.of(loggedInUserProfile));

        final FundWithUserDto result = mapper.map(refund);

        assertThat(result.getFunder()).isEqualTo(refunderAddress);
        assertThat(result.getFunderAddress()).isEqualTo(refunderAddress);
        assertThat(result.getFndFunds()).isNull();
        assertThat(result.getOtherFunds().getTokenSymbol()).isEqualTo(tokenValueDto.getTokenSymbol());
        assertThat(result.getOtherFunds().getTokenAddress()).isEqualTo(tokenValueDto.getTokenAddress());
        assertThat(result.getOtherFunds().getTotalAmount()).isEqualTo(tokenValueDto.getTotalAmount().negate());
        assertThat(result.isLoggedInUser()).isFalse();
        assertThat(result.getTimestamp()).isEqualToIgnoringMinutes(LocalDateTime.now());
        assertThat(result.isRefund()).isTrue();
    }
}
