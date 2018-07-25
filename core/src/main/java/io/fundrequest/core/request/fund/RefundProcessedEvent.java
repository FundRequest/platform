package io.fundrequest.core.request.fund;

import io.fundrequest.core.request.fund.domain.Refund;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class RefundProcessedEvent {
    private final Refund refund;
}
