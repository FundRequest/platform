package io.fundrequest.core.request.fund;

import io.fundrequest.core.request.fund.command.FundsAddedCommand;
import io.fundrequest.core.request.fund.dto.FundDto;
import io.fundrequest.core.request.fund.dto.TotalFundDto;

import java.util.List;
import java.util.Map;

public interface FundService {
    List<FundDto> findAll();

    List<FundDto> findAll(Iterable<Long> ids);

    FundDto findOne(Long id);

    List<FundDto> findByRequestId(Long requestId);

    List<TotalFundDto> getTotalFundsForRequest(Long requestId);

    Map<Long, List<FundDto>> findByRequestIds(List<Long> requestIds);

    void addFunds(FundsAddedCommand command);
}
