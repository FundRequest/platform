package io.fundreqest.platform.tweb.profile.linkedin;


import io.fundrequest.platform.profile.linkedin.LinkedInService;
import io.fundrequest.platform.profile.linkedin.dto.LinkedInPostDto;
import io.fundrequest.platform.profile.linkedin.dto.LinkedInVerificationDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.security.Principal;

@RequestMapping(value = "/bounties/linkedin")
@Controller
public class LinkedInBountyController {

    private LinkedInService linkedInService;

    public LinkedInBountyController(LinkedInService linkedInService) {
        this.linkedInService = linkedInService;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    public void verify(Principal principal, @ModelAttribute("post-id") Long postId) {
        linkedInService.postLinkedInShare(principal, postId);
    }

    @GetMapping
    public LinkedInVerificationDto getVerification(Principal principal) {
        return linkedInService.getVerification(principal);
    }

    @GetMapping(value = "/random-post", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public LinkedInPostDto blah() {
        return linkedInService.getRandomPost();
    }
}
