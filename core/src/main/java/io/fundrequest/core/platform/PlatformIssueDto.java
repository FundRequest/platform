package io.fundrequest.core.platform;

import io.fundrequest.core.request.domain.Platform;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class PlatformIssueDto {

    private Platform platform;
    private String platformId;
    private PlatformIssueStatus status;
}
