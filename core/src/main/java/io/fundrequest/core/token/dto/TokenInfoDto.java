package io.fundrequest.core.token.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenInfoDto {
    private String address;

    private String symbol;

    private String name;

    private int decimals;
}
