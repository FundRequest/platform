package io.fundrequest.core.request.command;

import io.fundrequest.core.request.domain.RequestStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class UpdateRequestStatusCommand {
    private final Long requestId;
    private final RequestStatus newStatus;
}
