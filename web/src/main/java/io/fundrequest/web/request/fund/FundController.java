package io.fundrequest.web.request.fund;

import io.fundrequest.core.request.fund.FundService;
import io.fundrequest.core.request.fund.command.AddFundsCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

@RestController
public class FundController {

    private FundService fundService;

    @Autowired
    public FundController(FundService fundService) {
        this.fundService = fundService;
    }

    @PostMapping("/requests/{requestId}/funds")
    public void addFunds(Principal principal, @Valid AddFundsCommand command) {
        fundService.addFunds(principal, command);
    }
}
