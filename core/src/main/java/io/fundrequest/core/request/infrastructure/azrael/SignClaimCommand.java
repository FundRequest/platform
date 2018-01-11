package io.fundrequest.core.request.infrastructure.azrael;

import org.hibernate.validator.constraints.NotEmpty;

import java.util.Objects;

public class SignClaimCommand {

    @NotEmpty
    private String platform;
    @NotEmpty
    private String platformId;
    @NotEmpty
    private String solver;
    @NotEmpty
    private String address;

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

    public String getSolver() {
        return solver;
    }

    public void setSolver(String solver) {
        this.solver = solver;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SignClaimCommand that = (SignClaimCommand) o;
        return Objects.equals(platform, that.platform) &&
                Objects.equals(platformId, that.platformId) &&
                Objects.equals(solver, that.solver) &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {

        return Objects.hash(platform, platformId, solver, address);
    }
}
