package io.fundrequest.core.request.fund.dto;

import io.fundrequest.core.infrastructure.SecurityContextService;
import io.fundrequest.core.infrastructure.mapping.BaseMapper;
import io.fundrequest.core.request.fund.domain.Refund;
import io.fundrequest.core.token.dto.TokenValueDto;
import io.fundrequest.core.token.mapper.TokenValueDtoMapper;
import io.fundrequest.platform.profile.profile.ProfileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class RefundToFundWithUserDtoMapper implements BaseMapper<Refund, FundWithUserDto> {
    private static final String FND_TOKEN_SYMBOL = "FND";

    private final ProfileService profileService;
    private final TokenValueDtoMapper tokenValueDtoMapper;
    private final SecurityContextService securityContextService;


    public RefundToFundWithUserDtoMapper(final ProfileService profileService,
                                         final TokenValueDtoMapper tokenValueDtoMapper,
                                         final SecurityContextService securityContextService) {
        this.profileService = profileService;
        this.tokenValueDtoMapper = tokenValueDtoMapper;
        this.securityContextService = securityContextService;
    }

    @Override
    public FundWithUserDto map(final Refund refund) {
        final TokenValueDto tokenValueDto = negate(tokenValueDtoMapper.map(refund.getTokenValue()));
        final String funderNameOrAddress = StringUtils.isNotBlank(refund.getRequestedBy())
                                           ? profileService.getUserProfile(refund.getRequestedBy()).getName()
                                           : refund.getFunderAddress();
        final boolean isFundedByLoggedInUser = isFundedByLoggedInUser(refund.getRequestedBy(), refund.getFunderAddress());
        return tokenValueDto == null ? null : FundWithUserDto.builder()
                                                             .funder(funderNameOrAddress)
                                                             .funderAddress(refund.getFunderAddress())
                                                             .fndFunds(getFndFunds(tokenValueDto))
                                                             .otherFunds(getOtherFunds(tokenValueDto))
                                                             .isLoggedInUser(isFundedByLoggedInUser)
                                                             .timestamp(refund.getCreationDate())
                                                             .isRefund(true)
                                                             .build();
    }

    private TokenValueDto negate(final TokenValueDto tokenValueDto) {
        return TokenValueDto.builder()
                            .tokenSymbol(tokenValueDto.getTokenSymbol())
                            .tokenAddress(tokenValueDto.getTokenAddress())
                            .totalAmount(tokenValueDto.getTotalAmount().negate())
                            .build();
    }

    private boolean isFundedByLoggedInUser(final String funderUserId, final String funderAddress) {
        return securityContextService.getLoggedInUserProfile()
                                     .filter(loggedInUserProfile -> loggedInUserProfile.getId().equals(funderUserId))
                                     .filter(loggedInUserProfile -> funderAddress.equalsIgnoreCase(loggedInUserProfile.getEtherAddress()))
                                     .isPresent();
    }

    private TokenValueDto getFndFunds(final TokenValueDto tokenValueDto) {
        return hasFNDTokenSymbol(tokenValueDto) ? tokenValueDto : null;
    }

    private TokenValueDto getOtherFunds(final TokenValueDto tokenValueDto) {
        return !hasFNDTokenSymbol(tokenValueDto) ? tokenValueDto : null;
    }

    private boolean hasFNDTokenSymbol(TokenValueDto tokenValueDto) {
        return FND_TOKEN_SYMBOL.equalsIgnoreCase(tokenValueDto.getTokenSymbol());
    }

}
