package io.fundrequest.core.contract.domain;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.Contract;

import java.math.BigInteger;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class FundRequestContract extends Contract {

    public FundRequestContract(final String contractAddress, final Web3j web3j) {
        super("", contractAddress, web3j, Credentials.create(ECKeyPair.create(BigInteger.ONE)), BigInteger.ONE, BigInteger.ZERO);
    }

    public RemoteCall<String> fundRepository() {
        Function function = new Function("fundRepository",
                                         emptyList(),
                                         singletonList(new TypeReference<Address>() {
                                         }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> claimRepository() {
        Function function = new Function("claimRepository",
                                         emptyList(),
                                         singletonList(new TypeReference<Address>() {
                                         }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }
}
