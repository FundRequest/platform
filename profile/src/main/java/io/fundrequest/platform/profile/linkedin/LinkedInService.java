package io.fundrequest.platform.profile.linkedin;

import io.fundrequest.platform.profile.linkedin.dto.LinkedInVerificationDto;

import java.security.Principal;

public interface LinkedInService {

    LinkedInVerificationDto getVerification(Principal principal);

}
