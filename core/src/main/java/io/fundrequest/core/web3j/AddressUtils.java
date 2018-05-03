package io.fundrequest.core.web3j;

public class AddressUtils {
    public static String prettify(final String address) {
        if (!address.startsWith("0x")) {
            return "0x" + address;
        } else {
            return address;
        }
    }
}
