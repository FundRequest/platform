package io.fundrequest.common.infrastructure.mav.dto;

import lombok.Data;

@Data
public class AlertDto {
    private final String type;
    private final String msg;
}
