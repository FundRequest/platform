package io.fundrequest.core.platform;

import io.fundrequest.core.request.domain.Platform;

import java.util.Optional;

public interface PlatformIssueService {

    Optional<PlatformIssueDto> findBy(Platform platform, String platformId);

}
