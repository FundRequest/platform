package io.fundrequest.core.request.fund;

import io.fundrequest.core.request.fund.command.AddFundsCommand;
import io.fundrequest.core.request.fund.dto.FundDto;

import java.util.List;

public interface FundService {
    List<FundDto> findAll();

    List<FundDto> findAll(Iterable<Long> ids);

    FundDto findOne(Long id);

    void addFunds(AddFundsCommand command);
}
