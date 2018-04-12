package io.fundrequest.platform.profile.linkedin;

import io.fundrequest.platform.profile.linkedin.dto.LinkedInPostDto;
import io.fundrequest.platform.profile.linkedin.dto.LinkedInVerificationDto;

import java.security.Principal;

public interface LinkedInService {

    LinkedInVerificationDto getVerification(Principal principal);

    void postLinkedInShare(Principal principal, Long postId);

    LinkedInPostDto getRandomPost();
}
