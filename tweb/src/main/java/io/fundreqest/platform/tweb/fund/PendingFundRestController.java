package io.fundreqest.platform.tweb.fund;

import io.fundreqest.platform.tweb.fund.command.PendingFundDto;
import io.fundrequest.core.fund.PendingFundService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/rest/pending-fund")
@Slf4j
public class PendingFundRestController {

    private final PendingFundService pendingFundService;

    public PendingFundRestController(final PendingFundService pendingFundService) {
        this.pendingFundService = pendingFundService;
    }

    @PostMapping
    public HttpStatus postPendingFund(@RequestBody PendingFundDto pendingFundDto,
                                      final Principal principal) {
        log.debug(pendingFundDto.toString());
        pendingFundService.save(pendingFundDto.toPendingFund(principal));
        return HttpStatus.OK;
    }

}
