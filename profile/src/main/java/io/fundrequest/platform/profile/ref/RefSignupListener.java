package io.fundrequest.platform.profile.ref;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RefSignupListener {

    private ReferralService referralService;

    public RefSignupListener(ReferralService referralService) {
        this.referralService = referralService;
    }

    @EventListener
    public void onSignup(RefSignupEvent refSignupEvent) {
        referralService.createNewRef(
                CreateRefCommand.builder().principal(refSignupEvent.getPrincipal()).ref(refSignupEvent.getRef()).build()
                                    );
    }


}
