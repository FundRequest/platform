package io.fundrequest.core.request.infrastructure.azrael;


import io.fundrequest.core.transactions.TransactionStatus;

public interface AzraelClient {

    ClaimSignature getSignature(SignClaimCommand signClaimCommand);

    ClaimTransaction submitClaim(ClaimSignature claimSignature);

    TransactionStatus getTransactionStatus(String hash);

    RefundTransaction submitRefund(RefundCommand refundCommand);
}
