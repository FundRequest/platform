package io.fundrequest.platform.profile.arkane;

import java.util.List;

public class WalletsResult {
    private boolean success;
    private List<Wallet> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Wallet> getResult() {
        return result;
    }

    public void setResult(List<Wallet> result) {
        this.result = result;
    }
}
