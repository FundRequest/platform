package io.fundreqest.platform.tweb.request.dto;

import lombok.Data;

@Data
public class ClaimView {
    private final boolean claimable;
    private final String claimableByPlatformUser;
}
