package io.fundrequest.core.request.fund;

import io.fundrequest.core.request.fund.command.AddFundsCommand;
import io.fundrequest.core.request.fund.dto.FundDto;

import java.security.Principal;
import java.util.List;

public interface FundService {
    List<FundDto> findAll();

    void addFunds(Principal funder, AddFundsCommand command);
}
