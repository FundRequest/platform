package io.fundrequest.platform.profile.ref.infrastructure.mav;

import io.fundrequest.platform.profile.profile.ProfileService;
import io.fundrequest.platform.profile.profile.dto.UserProfile;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
public class ProfileControllerAdvice {

    private ProfileService profileService;

    public ProfileControllerAdvice(final ProfileService profileService) {
        this.profileService = profileService;
    }

    @ModelAttribute
    public void addAttributes(Model model, Principal principal) {
        if (principal != null) {
            final UserProfile userProfile = profileService.getUserProfile(principal);
            model.addAttribute("profile", userProfile);
        }
    }
}
