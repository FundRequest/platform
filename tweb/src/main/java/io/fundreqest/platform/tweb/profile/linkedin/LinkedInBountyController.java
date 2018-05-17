package io.fundreqest.platform.tweb.profile.linkedin;


import io.fundrequest.platform.profile.linkedin.LinkedInService;
import io.fundrequest.platform.profile.linkedin.dto.LinkedInVerificationDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@RequestMapping(value = "/bounties/linkedin")
@Controller
public class LinkedInBountyController {

    private LinkedInService linkedInService;

    public LinkedInBountyController(LinkedInService linkedInService) {
        this.linkedInService = linkedInService;
    }

    @GetMapping
    public LinkedInVerificationDto getVerification(Principal principal) {
        return linkedInService.getVerification(principal);
    }

}
