package io.fundrequest.core.token.dto;

import lombok.Data;

@Data
public class TokenInfoDto {
    private String address;

    private String symbol;

    private String name;

    private Long decimals;
}
