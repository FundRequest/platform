package io.fundrequest.core.request.claim;

import io.fundrequest.core.request.domain.Platform;

public class SignedClaim {
    private String solver;
    private String solverAddress;
    private Platform platform;
    private String platformId;
    private String r;
    private String s;
    private Integer v;

    public SignedClaim(String solver, String solverAddress, Platform platform, String platformId, String r, String s, Integer v) {
        this.solver = solver;
        this.solverAddress = solverAddress;
        this.platform = platform;
        this.platformId = platformId;
        this.r = r;
        this.s = s;
        this.v = v;
    }

    public String getSolver() {
        return solver;
    }

    public String getSolverAddress() {
        return solverAddress;
    }

    public Platform getPlatform() {
        return platform;
    }

    public String getPlatformId() {
        return platformId;
    }

    public String getR() {
        return r;
    }

    public String getS() {
        return s;
    }

    public Integer getV() {
        return v;
    }
}
