package io.fundrequest.platform.profile.ref.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShortUrlResult {
    private String kind;
    private String id;
    private String longUrl;
}
