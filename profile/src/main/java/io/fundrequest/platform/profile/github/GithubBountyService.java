package io.fundrequest.platform.profile.github;

import io.fundrequest.platform.profile.profile.dto.GithubVerificationDto;

import java.security.Principal;

public interface GithubBountyService {

    GithubVerificationDto getVerification(Principal principal);
}
