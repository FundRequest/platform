package io.fundrequest.platform.profile.stackoverflow;

import io.fundrequest.platform.profile.stackoverflow.dto.StackOverflowVerificationDto;

import java.security.Principal;

public interface StackOverflowBountyService {
    StackOverflowVerificationDto getVerification(Principal principal);
}
