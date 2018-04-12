package io.fundrequest.core.contract.domain;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.Web3j;
import org.web3j.tx.Contract;

import java.math.BigInteger;

public class ClaimRepositoryContract extends Contract {

    public ClaimRepositoryContract(final String contractAddress, final Web3j web3j) {
        super("", contractAddress, web3j, Credentials.create(ECKeyPair.create(BigInteger.ONE)), BigInteger.ONE, BigInteger.ZERO);
    }
}
