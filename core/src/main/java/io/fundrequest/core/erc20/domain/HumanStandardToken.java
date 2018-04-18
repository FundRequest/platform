package io.fundrequest.core.erc20.domain;

import lombok.extern.slf4j.Slf4j;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;

import java.math.BigInteger;
import java.util.Collections;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;


@Slf4j
public final class HumanStandardToken extends Contract {

    private static final Credentials DUMMY = Credentials.create("0x0");


    private static final String BINARY
            =
            "606060405260408051908101604052600481527f48302e31000000000000000000000000000000000000000000000000000000006020820152600690805161004b9291602001906100e7565b50341561005757600080fd5b6040516109f83803806109f8833981016040528080519190602001805182019190602001805191906020018051600160a060020a0333166000908152600160205260408120879055869055909101905060038380516100ba9291602001906100e7565b506004805460ff191660ff841617905560058180516100dd9291602001906100e7565b5050505050610182565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061012857805160ff1916838001178555610155565b82800160010185558215610155579182015b8281111561015557825182559160200191906001019061013a565b50610161929150610165565b5090565b61017f91905b80821115610161576000815560010161016b565b90565b610867806101916000396000f300606060405236156100935763ffffffff60e060020a60003504166306fdde0381146100a3578063095ea7b31461012d57806318160ddd1461016357806323b872dd14610188578063313ce567146101b057806354fd4d50146101d957806370a08231146101ec57806395d89b411461020b578063a9059cbb1461021e578063cae9ca5114610240578063dd62ed3e146102a5575b341561009e57600080fd5b600080fd5b34156100ae57600080fd5b6100b66102ca565b60405160208082528190810183818151815260200191508051906020019080838360005b838110156100f25780820151838201526020016100da565b50505050905090810190601f16801561011f5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b341561013857600080fd5b61014f600160a060020a0360043516602435610368565b604051901515815260200160405180910390f35b341561016e57600080fd5b6101766103d5565b60405190815260200160405180910390f35b341561019357600080fd5b61014f600160a060020a03600435811690602435166044356103db565b34156101bb57600080fd5b6101c36104d3565b60405160ff909116815260200160405180910390f35b34156101e457600080fd5b6100b66104dc565b34156101f757600080fd5b610176600160a060020a0360043516610547565b341561021657600080fd5b6100b6610562565b341561022957600080fd5b61014f600160a060020a03600435166024356105cd565b341561024b57600080fd5b61014f60048035600160a060020a03169060248035919060649060443590810190830135806020601f8201819004810201604051908101604052818152929190602084018383808284375094965061067095505050505050565b34156102b057600080fd5b610176600160a060020a0360043581169060243516610810565b60038054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103605780601f1061033557610100808354040283529160200191610360565b820191906000526020600020905b81548152906001019060200180831161034357829003601f168201915b505050505081565b600160a060020a03338116600081815260026020908152604080832094871680845294909152808220859055909291907f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b9259085905190815260200160405180910390a35060015b92915050565b60005481565b600160a060020a03831660009081526001602052604081205482901080159061042b5750600160a060020a0380851660009081526002602090815260408083203390941683529290522054829010155b80156104375750600082115b156104c857600160a060020a03808416600081815260016020908152604080832080548801905588851680845281842080548990039055600283528184203390961684529490915290819020805486900390559091907fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef9085905190815260200160405180910390a35060016104cc565b5060005b9392505050565b60045460ff1681565b60068054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103605780601f1061033557610100808354040283529160200191610360565b600160a060020a031660009081526001602052604090205490565b60058054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103605780601f1061033557610100808354040283529160200191610360565b600160a060020a0333166000908152600160205260408120548290108015906105f65750600082115b1561066857600160a060020a033381166000818152600160205260408082208054879003905592861680825290839020805486019055917fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef9085905190815260200160405180910390a35060016103cf565b5060006103cf565b600160a060020a03338116600081815260026020908152604080832094881680845294909152808220869055909291907f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b9259086905190815260200160405180910390a383600160a060020a03166040517f72656365697665417070726f76616c28616464726573732c75696e743235362c81527f616464726573732c6279746573290000000000000000000000000000000000006020820152602e01604051809103902060e060020a9004338530866040518563ffffffff1660e060020a0281526004018085600160a060020a0316600160a060020a0316815260200184815260200183600160a060020a0316600160a060020a03168152602001828051906020019080838360005b838110156107b1578082015183820152602001610799565b50505050905090810190601f1680156107de5780820380516001836020036101000a031916815260200191505b5094505050505060006040518083038160008761646e5a03f192505050151561080657600080fd5b5060019392505050565b600160a060020a039182166000908152600260209081526040808320939094168252919091522054905600a165627a7a7230582099618d3c5204845d688b605708cc5a47a962ec312a8c774ab67f9c9e40c415970029";

    private HumanStandardToken(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    private RemoteCall<String> nameAsync() {
        Function function = new Function("name",
                                         emptyList(),
                                         singletonList(new TypeReference<Utf8String>() {
                                         }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public String name() {
        try {
            return nameAsync().send();
        } catch (Exception e) {
            log.error("Unable to fetch name for erc20", e);
            return "Unknown ERC20";
        }
    }

    public RemoteCall<TransactionReceipt> approve(String _spender, BigInteger _value) {
        Function function = new Function(
                "approve",
                asList(new org.web3j.abi.datatypes.Address(_spender),
                       new org.web3j.abi.datatypes.generated.Uint256(_value)),
                emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> totalSupply() {
        Function function = new Function("totalSupply",
                                         emptyList(),
                                         singletonList(new TypeReference<Uint256>() {
                                         }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> transferFrom(String _from, String _to, BigInteger _value) {
        Function function = new Function(
                "transferFrom",
                asList(new org.web3j.abi.datatypes.Address(_from),
                       new org.web3j.abi.datatypes.Address(_to),
                       new org.web3j.abi.datatypes.generated.Uint256(_value)),
                emptyList());
        return executeRemoteCallTransaction(function);
    }

    private RemoteCall<BigInteger> decimalsAsync() {
        Function function = new Function("decimals",
                                         emptyList(),
                                         singletonList(new TypeReference<Uint8>() {
                                         }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public int decimals() {
        try {
            return decimalsAsync().send().intValue();
        } catch (Exception e) {
            log.error("Unable to fetch decimals", e);
            return 18;
        }
    }

    public RemoteCall<String> version() {
        Function function = new Function("version",
                                         emptyList(),
                                         singletonList(new TypeReference<Utf8String>() {
                                         }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> balanceOf(String _owner) {
        Function function = new Function("balanceOf",
                                         singletonList(new Address(_owner)),
                                         singletonList(new TypeReference<Uint256>() {
                                         }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    private RemoteCall<String> symbolAsync() {
        Function function = new Function("symbol",
                                         emptyList(),
                                         singletonList(new TypeReference<Utf8String>() {
                                         }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public String symbol() {
        try {
            return symbolAsync().send();
        } catch (Exception e) {
            log.debug("Unable to fetch symbol for erc20", e);
            return "ERC20";
        }
    }

    public RemoteCall<TransactionReceipt> transfer(String _to, BigInteger _value) {
        Function function = new Function(
                "transfer",
                asList(new org.web3j.abi.datatypes.Address(_to),
                       new org.web3j.abi.datatypes.generated.Uint256(_value)),
                emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> approveAndCall(String _spender, BigInteger _value, byte[] _extraData) {
        Function function = new Function(
                "approveAndCall",
                asList(new org.web3j.abi.datatypes.Address(_spender),
                       new org.web3j.abi.datatypes.generated.Uint256(_value),
                       new org.web3j.abi.datatypes.DynamicBytes(_extraData)),
                emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> allowance(String _owner, String _spender) {
        Function function = new Function("allowance",
                                         asList(new org.web3j.abi.datatypes.Address(_owner),
                                                new org.web3j.abi.datatypes.Address(_spender)),
                                         Collections.singletonList(new TypeReference<Uint256>() {
                                         }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public static HumanStandardToken load(String contractAddress, Web3j web3j) {
        return new HumanStandardToken(contractAddress, web3j, DUMMY, BigInteger.ZERO, BigInteger.ZERO);
    }
}