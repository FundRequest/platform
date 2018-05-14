package io.fundrequest.core.request.fund.domain;

import io.fundrequest.core.request.domain.IssueInformation;
import io.fundrequest.core.util.ReflectionUtils;

import java.math.BigInteger;
import java.time.LocalDateTime;

public class PendingFundMother {

    public static PendingFund aPendingFund() {
        final PendingFund pendingFund = PendingFund.builder()
                                                   .amount(BigInteger.TEN)
                                                   .fromAddress("fromAddress")
                                                   .tokenAddress("tokenAddress")
                                                   .description("description")
                                                   .transactionhash("txHash")
                                                   .issueInformation(IssueInformation.builder().build())
                                                   .build();
        ReflectionUtils.set(pendingFund, "creationDate", LocalDateTime.now());
        return pendingFund;
    }

}