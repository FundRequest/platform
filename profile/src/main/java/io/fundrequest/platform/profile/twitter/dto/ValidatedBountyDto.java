package io.fundrequest.platform.profile.twitter.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValidatedBountyDto {

    @Builder.Default
    public boolean validated = false;
    @Builder.Default
    public String message = "";
}