package io.fundrequest.core.messaging.dto;

public class FundedEthDto {

    private String from;
    private String amount;
    private String data;
    private String user;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "FundedEthDto{" +
                "from='" + from + '\'' +
                ", amount='" + amount + '\'' +
                ", data='" + data + '\'' +
                ", user='" + user + '\'' +
                '}';
    }
}
