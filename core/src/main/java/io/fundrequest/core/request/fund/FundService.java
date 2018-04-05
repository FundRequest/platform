package io.fundrequest.core.request.fund;

import io.fundrequest.core.request.fund.command.FundsAddedCommand;
import io.fundrequest.core.request.fund.dto.FundDto;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface FundService {
    List<FundDto> findAll();

    List<FundDto> findAll(Iterable<Long> ids);

    FundDto findOne(Long id);

    List<FundDto> findByRequestId(Long requestId);

    @Transactional(readOnly = true)
    BigDecimal getTotalFundsForRequest(Long requestId);

    Map<Long, List<FundDto>> findByRequestIds(List<Long> requestIds);

    void addFunds(FundsAddedCommand command);
}
