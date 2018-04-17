package io.fundrequest.core.request.fund;

import io.fundrequest.core.request.fund.command.FundsAddedCommand;
import io.fundrequest.core.request.fund.dto.FundDto;
import io.fundrequest.core.request.fund.dto.TotalFundDto;
import org.springframework.cache.annotation.CacheEvict;

import java.util.List;
import java.util.Map;

public interface FundService {
    List<FundDto> findAll();

    List<FundDto> findAll(Iterable<Long> ids);

    FundDto findOne(Long id);

    List<TotalFundDto> getTotalFundsForRequest(Long requestId);

    void clearTotalFundsCache(Long requestId);

    void addFunds(FundsAddedCommand command);
}
