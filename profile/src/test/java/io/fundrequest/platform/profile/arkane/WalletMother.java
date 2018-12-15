package io.fundrequest.platform.profile.arkane;

public final class WalletMother {

    public static Wallet aWallet() {
        Wallet wallet = new Wallet();
        wallet.setAddress("0x0000");
        wallet.setDescription("description");
        wallet.setId("id");
        return wallet;
    }

}