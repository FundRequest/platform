package io.fundrequest.core.request.fund.dto;

import io.fundrequest.core.infrastructure.SecurityContextService;
import io.fundrequest.core.request.fund.domain.Fund;
import io.fundrequest.core.token.dto.TokenValueDto;
import io.fundrequest.core.token.mapper.TokenValueDtoMapper;
import io.fundrequest.core.token.model.TokenValue;
import io.fundrequest.platform.profile.profile.ProfileService;
import io.fundrequest.platform.profile.profile.dto.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FundToFundWithUserDtoMapperTest {

    private FundToFundWithUserDtoMapper mapper;

    private ProfileService profileService;
    private TokenValueDtoMapper tokenValueDtoMapper;
    private SecurityContextService securityContextService;

    @BeforeEach
    void setUp() {
        profileService = mock(ProfileService.class);
        tokenValueDtoMapper = mock(TokenValueDtoMapper.class);
        securityContextService = mock(SecurityContextService.class);
        mapper = new FundToFundWithUserDtoMapper(profileService, tokenValueDtoMapper, securityContextService);
    }

    @Test
    void map_otherToken() {
        final String funderUserId = "gdhfgj";
        final String funderAddress = "0x45HDF80";
        final String funderUserName = "hgjgfk";
        final TokenValue tokenValue = mock(TokenValue.class);
        final Fund fund = Fund.builder()
                              .funderUserId(funderUserId)
                              .funderAddress(funderAddress)
                              .tokenValue(tokenValue)
                              .requestId(543L)
                              .timestamp(LocalDateTime.now()).blockchainEventId(238L)
                              .build();
        final TokenValueDto tokenValueDto = TokenValueDto.builder().tokenSymbol("SOT").build();
        final UserProfile fundUserProfile = UserProfile.builder().id(funderUserId).etherAddress(funderAddress).name(funderUserName).build();
        final UserProfile loggedInUserProfile = UserProfile.builder().id(funderUserId).etherAddress(funderAddress).name(funderUserName).build();


        when(tokenValueDtoMapper.map(tokenValue)).thenReturn(tokenValueDto);
        when(profileService.getUserProfile(funderUserId)).thenReturn(fundUserProfile);
        when(securityContextService.getLoggedInUserProfile()).thenReturn(Optional.of(loggedInUserProfile));

        final FundWithUserDto result = mapper.map(fund);

        assertThat(result.getFunder()).isEqualTo(funderUserName);
        assertThat(result.getFunderAddress()).isEqualTo(funderAddress.toLowerCase());
        assertThat(result.getFndFunds()).isNull();
        assertThat(result.getOtherFunds()).isSameAs(tokenValueDto);
        assertThat(result.isLoggedInUser()).isTrue();
        assertThat(result.getTimestamp()).isEqualToIgnoringMinutes(LocalDateTime.now());
        assertThat(result.isRefund()).isFalse();
    }

    @Test
    void map_FND() {
        final String funderUserId = "gdhfgj";
        final String funderAddress = "0x45HDF80";
        final String funderUserName = "hgjgfk";
        final TokenValue tokenValue = mock(TokenValue.class);
        final Fund fund = Fund.builder()
                              .funderUserId(funderUserId)
                              .funderAddress(funderAddress)
                              .tokenValue(tokenValue)
                              .requestId(543L)
                              .timestamp(LocalDateTime.now()).blockchainEventId(238L)
                              .build();
        final TokenValueDto tokenValueDto = TokenValueDto.builder().tokenSymbol("FND").build();
        final UserProfile fundUserProfile = UserProfile.builder().id(funderUserId).etherAddress(funderAddress).name(funderUserName).build();
        final UserProfile loggedInUserProfile = UserProfile.builder().id(funderUserId).etherAddress(funderAddress).name(funderUserName).build();


        when(tokenValueDtoMapper.map(tokenValue)).thenReturn(tokenValueDto);
        when(profileService.getUserProfile(funderUserId)).thenReturn(fundUserProfile);
        when(securityContextService.getLoggedInUserProfile()).thenReturn(Optional.of(loggedInUserProfile));

        final FundWithUserDto result = mapper.map(fund);

        assertThat(result.getFunder()).isEqualTo(funderUserName);
        assertThat(result.getFunderAddress()).isEqualTo(funderAddress.toLowerCase());
        assertThat(result.getFndFunds()).isSameAs(tokenValueDto);
        assertThat(result.getOtherFunds()).isNull();
        assertThat(result.isLoggedInUser()).isTrue();
        assertThat(result.getTimestamp()).isEqualToIgnoringMinutes(LocalDateTime.now());
        assertThat(result.isRefund()).isFalse();
    }

    @Test
    void map_loggedInUserDifferentUser() {
        final String funderUserId = "gdhfgj";
        final String funderAddress = "0x45HDF80";
        final String funderUserName = "hgjgfk";
        final TokenValue tokenValue = mock(TokenValue.class);
        final Fund fund = Fund.builder()
                              .funderUserId(funderUserId)
                              .funderAddress(funderAddress)
                              .tokenValue(tokenValue)
                              .requestId(543L)
                              .timestamp(LocalDateTime.now()).blockchainEventId(238L)
                              .build();
        final TokenValueDto tokenValueDto = TokenValueDto.builder().tokenSymbol("SOT").build();
        final UserProfile fundUserProfile = UserProfile.builder().id(funderUserId).etherAddress(funderAddress).name(funderUserName).build();
        final UserProfile loggedInUserProfile = UserProfile.builder().id("fgh").etherAddress("0xrdtdr").name(funderUserName).build();


        when(tokenValueDtoMapper.map(tokenValue)).thenReturn(tokenValueDto);
        when(profileService.getUserProfile(funderUserId)).thenReturn(fundUserProfile);
        when(securityContextService.getLoggedInUserProfile()).thenReturn(Optional.of(loggedInUserProfile));

        final FundWithUserDto result = mapper.map(fund);

        assertThat(result.getFunder()).isEqualTo(funderUserName);
        assertThat(result.getFunderAddress()).isEqualTo(funderAddress.toLowerCase());
        assertThat(result.getFndFunds()).isNull();
        assertThat(result.getOtherFunds()).isSameAs(tokenValueDto);
        assertThat(result.isLoggedInUser()).isFalse();
        assertThat(result.getTimestamp()).isEqualToIgnoringMinutes(LocalDateTime.now());
        assertThat(result.isRefund()).isFalse();
    }

    @Test
    void map_loggedInUserDifferentAddress() {
        final String funderUserId = "gdhfgj";
        final String funderAddress = "0x45HDF80";
        final String funderUserName = "hgjgfk";
        final TokenValue tokenValue = mock(TokenValue.class);
        final Fund fund = Fund.builder()
                              .funderUserId(funderUserId)
                              .funderAddress(funderAddress)
                              .tokenValue(tokenValue)
                              .requestId(543L)
                              .timestamp(LocalDateTime.now()).blockchainEventId(238L)
                              .build();
        final TokenValueDto tokenValueDto = TokenValueDto.builder().tokenSymbol("SOT").build();
        final UserProfile fundUserProfile = UserProfile.builder().id(funderUserId).etherAddress(funderAddress).name(funderUserName).build();
        final UserProfile loggedInUserProfile = UserProfile.builder().id(funderUserId).etherAddress("0xrdtdr").name(funderUserName).build();


        when(tokenValueDtoMapper.map(tokenValue)).thenReturn(tokenValueDto);
        when(profileService.getUserProfile(funderUserId)).thenReturn(fundUserProfile);
        when(securityContextService.getLoggedInUserProfile()).thenReturn(Optional.of(loggedInUserProfile));

        final FundWithUserDto result = mapper.map(fund);

        assertThat(result.getFunder()).isEqualTo(funderUserName);
        assertThat(result.getFunderAddress()).isEqualTo(funderAddress.toLowerCase());
        assertThat(result.getFndFunds()).isNull();
        assertThat(result.getOtherFunds()).isSameAs(tokenValueDto);
        assertThat(result.isLoggedInUser()).isFalse();
        assertThat(result.getTimestamp()).isEqualToIgnoringMinutes(LocalDateTime.now());
        assertThat(result.isRefund()).isFalse();
    }

    @Test
    void map_noFunderUserId() {
        final String funderAddress = "0x45HDF80";
        final TokenValue tokenValue = mock(TokenValue.class);
        final Fund fund = Fund.builder()
                              .funderAddress(funderAddress)
                              .tokenValue(tokenValue)
                              .requestId(543L)
                              .timestamp(LocalDateTime.now()).blockchainEventId(238L)
                              .build();
        final TokenValueDto tokenValueDto = TokenValueDto.builder().tokenSymbol("SOT").build();
        final UserProfile loggedInUserProfile = UserProfile.builder().id("efsgfbg").etherAddress("0xrdtdr").name("sfas").build();


        when(tokenValueDtoMapper.map(tokenValue)).thenReturn(tokenValueDto);
        when(securityContextService.getLoggedInUserProfile()).thenReturn(Optional.of(loggedInUserProfile));

        final FundWithUserDto result = mapper.map(fund);

        assertThat(result.getFunder()).isEqualTo(funderAddress.toLowerCase());
        assertThat(result.getFunderAddress()).isEqualTo(funderAddress.toLowerCase());
        assertThat(result.getFndFunds()).isNull();
        assertThat(result.getOtherFunds()).isSameAs(tokenValueDto);
        assertThat(result.isLoggedInUser()).isFalse();
        assertThat(result.getTimestamp()).isEqualToIgnoringMinutes(LocalDateTime.now());
        assertThat(result.isRefund()).isFalse();
    }
}
