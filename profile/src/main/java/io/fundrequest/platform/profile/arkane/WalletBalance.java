package io.fundrequest.platform.profile.arkane;

public class WalletBalance {
    private String balance;
    private String gasBalance;
    private String rawBalance;
    private String rawGasBalance;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getGasBalance() {
        return gasBalance;
    }

    public void setGasBalance(String gasBalance) {
        this.gasBalance = gasBalance;
    }

    public String getRawBalance() {
        return rawBalance;
    }

    public void setRawBalance(String rawBalance) {
        this.rawBalance = rawBalance;
    }

    public String getRawGasBalance() {
        return rawGasBalance;
    }

    public void setRawGasBalance(String rawGasBalance) {
        this.rawGasBalance = rawGasBalance;
    }
}
