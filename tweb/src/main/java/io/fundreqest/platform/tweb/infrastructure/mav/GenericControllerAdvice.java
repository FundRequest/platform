package io.fundreqest.platform.tweb.infrastructure.mav;

import io.fundrequest.platform.profile.profile.ProfileService;
import io.fundrequest.platform.profile.profile.dto.UserProfile;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@ControllerAdvice
public class GenericControllerAdvice {

    private ProfileService profileService;

    public GenericControllerAdvice(ProfileService profileService) {
        this.profileService = profileService;
    }

    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request, Principal principal) {
        if (principal != null) {
            final UserProfile userProfile = profileService.getUserProfile(request, principal);
            model.addAttribute("profile", userProfile);
        }
    }
}
