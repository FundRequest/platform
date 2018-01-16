package io.fundrequest.core.request.command;

import io.fundrequest.core.request.domain.Platform;

import java.time.LocalDateTime;
import java.util.Objects;

public class RequestClaimedCommand {
    private Platform platform;
    private String platformId;
    private String solver;
    private LocalDateTime timestamp;

    public RequestClaimedCommand() {
    }

    public RequestClaimedCommand(Platform platform, String platformId, String solver, LocalDateTime timestamp) {
        this.platform = platform;
        this.platformId = platformId;
        this.solver = solver;
        this.timestamp = timestamp;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestClaimedCommand that = (RequestClaimedCommand) o;
        return platform == that.platform &&
                Objects.equals(platformId, that.platformId) &&
                Objects.equals(solver, that.solver) &&
                Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {

        return Objects.hash(platform, platformId, solver, timestamp);
    }
}
