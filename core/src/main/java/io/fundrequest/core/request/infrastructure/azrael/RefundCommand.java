package io.fundrequest.core.request.infrastructure.azrael;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefundCommand {
    private String platform;
    private String platformId;
    private String address;
}
