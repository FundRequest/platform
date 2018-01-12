package io.fundrequest.core.request.fund.messaging.dto;

public class ClaimedEthDto {
    private String transactionHash;
    private String solverAddress;
    private String platform;
    private String platformId;
    private String amount;
    private String solver;
    private long timestamp;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }


    public String getSolverAddress() {
        return solverAddress;
    }

    public void setSolverAddress(String solverAddress) {
        this.solverAddress = solverAddress;
    }

    public String getSolver() {
        return solver;
    }

    public void setSolver(String solver) {
        this.solver = solver;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
