package io.fundrequest.core.request.fund;

import io.fundrequest.core.request.fund.command.FundsAddedCommand;
import io.fundrequest.core.request.fund.dto.FundDto;
import io.fundrequest.core.request.fund.dto.FundsForRequestDto;
import io.fundrequest.core.token.dto.TokenValueDto;

import java.util.List;
import java.util.Optional;

public interface FundService {
    List<FundDto> findAll();

    List<FundDto> findAll(Iterable<Long> ids);

    FundDto findOne(Long id);

    List<TokenValueDto> getTotalFundsForRequest(Long requestId);

    FundsForRequestDto getFundsForRequestGroupedByFunder(Long requestId);

    void clearTotalFundsCache(Long requestId);

    void addFunds(FundsAddedCommand command);

    Optional<TokenValueDto> getFundsFor(Long requestId, String funderAddress, String tokenAddress);
}
