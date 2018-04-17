package io.fundreqest.platform.tweb.fund;

import io.fundreqest.platform.tweb.fund.command.PendingFundDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/pending-fund")
@Slf4j
public class PendingFundRestController {

    @PostMapping
    public HttpStatus postPendingFund(@RequestBody PendingFundDto pendingFundDto) {
        log.debug(pendingFundDto.toString());
        return HttpStatus.OK;
    }

}
