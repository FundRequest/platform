package io.fundreqest.platform.tweb.request.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ERC67FundDto {
    private String erc67Link;
}
