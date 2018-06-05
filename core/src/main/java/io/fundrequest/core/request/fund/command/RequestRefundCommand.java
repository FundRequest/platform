package io.fundrequest.core.request.fund.command;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestRefundCommand {

    private final Long requestId;

    private final String funderAddress;

    private final String r;

    private final String v;

    private final String s;
}
