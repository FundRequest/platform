package io.fundrequest.platform.profile.twitter.dto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValidatedBountyDto {
    public boolean validated = false;
    public String message = "";
}