package io.fundrequest.platform.intercom.claim;

import io.fundrequest.core.identity.IdentityAPIClient;
import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.claim.ClaimService;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.notification.dto.RequestClaimedNotificationDto;
import io.fundrequest.platform.intercom.IntercomApiClient;
import io.fundrequest.platform.intercom.builder.AdminBuilder;
import io.fundrequest.platform.intercom.builder.AdminMessageBuilder;
import io.fundrequest.platform.intercom.builder.UserBuilder;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Component
@ConditionalOnProperty(name = "io.fundrequest.notifications.notify-on-claim", havingValue = "true")
public class RequestClaimedUserNotificationHandler {

    private final String platformBasepath;
    private final ClaimService claimService;
    private final RequestService requestService;
    private final IdentityAPIClient identityAPIClient;
    private final ITemplateEngine githubTemplateEngine;
    private final IntercomApiClient intercomApiClient;
    private final String adminId;

    public RequestClaimedUserNotificationHandler(@Value("${io.fundrequest.intercom.message.admin-id}") final String adminId,
                                                 @Value("${io.fundrequest.platform.base-path:https://fundrequest.io}") final String platformBasepath,
                                                 final ClaimService claimService,
                                                 final RequestService requestService,
                                                 final IdentityAPIClient identityAPIClient,
                                                 final ITemplateEngine githubTemplateEngine,
                                                 final IntercomApiClient intercomApiClient) {
        this.platformBasepath = platformBasepath;
        this.claimService = claimService;
        this.requestService = requestService;
        this.identityAPIClient = identityAPIClient;
        this.githubTemplateEngine = githubTemplateEngine;
        this.intercomApiClient = intercomApiClient;
        this.adminId = adminId;
    }

    @EventListener
    @Async("taskExecutor")
    @Transactional(readOnly = true, propagation = REQUIRES_NEW)
    public void handle(final RequestClaimedNotificationDto notification) {
        claimService.findOne(notification.getClaimId())
                    .ifPresent(claim -> {
                        final RequestDto request = requestService.findRequest(notification.getRequestId());
                        final UserRepresentation user = identityAPIClient.findByIdentityProviderAndFederatedUsername(request.getIssueInformation().getPlatform().name().toLowerCase(), claim.getSolver());

                        final Context context = new Context();
                        context.setVariable("platformBasepath", platformBasepath);
                        context.setVariable("request", request);
                        final String messageBody = githubTemplateEngine.process("notification-templates/claimed-claimer-notification", context);

                        intercomApiClient.postMessage(AdminMessageBuilder.newInstanceWith()
                                                                         .messageType("email")
                                                                         .template("plain")
                                                                         .admin(AdminBuilder.newInstanceWith().id(adminId).build())
                                                                         .subject("Your reward has been transferred to your account")
                                                                         .body(messageBody)
                                                                         .user(UserBuilder.newInstanceWith().email(user.getEmail()).build())
                                                                         .build());
                    });
    }
}
