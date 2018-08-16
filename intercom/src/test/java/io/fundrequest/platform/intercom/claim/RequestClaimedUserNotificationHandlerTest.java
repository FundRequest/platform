package io.fundrequest.platform.intercom.claim;

import io.fundrequest.core.identity.IdentityAPIClient;
import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.claim.ClaimService;
import io.fundrequest.core.request.view.ClaimDtoMother;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.request.view.RequestDtoMother;
import io.fundrequest.notification.dto.RequestClaimedNotificationDto;
import io.fundrequest.platform.intercom.IntercomApiClient;
import io.fundrequest.platform.intercom.builder.AdminBuilder;
import io.fundrequest.platform.intercom.builder.AdminMessageBuilder;
import io.fundrequest.platform.intercom.builder.UserBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.representations.idm.UserRepresentation;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RequestClaimedUserNotificationHandlerTest {

    private RequestClaimedUserNotificationHandler handler;

    private String platformBasepath;
    private ClaimService claimService;
    private RequestService requestService;
    private IdentityAPIClient identityAPIClient;
    private ITemplateEngine githubTemplateEngine;
    private IntercomApiClient intercomApiClient;
    private String adminId;

    @BeforeEach
    void setUp() {
        platformBasepath = "http://dsgsagd";

        claimService = mock(ClaimService.class);
        requestService = mock(RequestService.class);
        identityAPIClient = mock(IdentityAPIClient.class);
        githubTemplateEngine = mock(ITemplateEngine.class);
        intercomApiClient = mock(IntercomApiClient.class);
        adminId = "dfhf765";
        handler = new RequestClaimedUserNotificationHandler(adminId, platformBasepath, claimService, requestService, identityAPIClient, githubTemplateEngine, intercomApiClient);
    }

    @Test
    void handle() {
        final long blockchainEventId = 2435L;
        final long requestId = 54657L;
        final long claimId = 7657L;
        final String solver = "afsg";
        final String userEmail = "gsfd@afsgb.com";
        final UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEmail(userEmail);
        final RequestClaimedNotificationDto notification = RequestClaimedNotificationDto.builder()
                                                                                        .blockchainEventId(blockchainEventId)
                                                                                        .requestId(requestId)
                                                                                        .claimId(claimId)
                                                                                        .date(LocalDateTime.now())
                                                                                        .build();
        final RequestDto requestDto = RequestDtoMother.fundRequestArea51();
        requestDto.setId(requestId);
        final String body = "szgsg";
        final Context context = new Context();
        context.setVariable("platformBasepath", platformBasepath);
        context.setVariable("request", requestDto);

        when(claimService.findOne(claimId)).thenReturn(Optional.of(ClaimDtoMother.aClaimDto().solver(solver).requestId(requestId).build()));
        when(requestService.findRequest(requestId)).thenReturn(requestDto);
        when(identityAPIClient.findByIdentityProviderAndFederatedUsername(requestDto.getIssueInformation().getPlatform().name().toLowerCase(), solver)).thenReturn(userRepresentation);
        when(githubTemplateEngine.process(eq("notification-templates/claimed-claimer-notification"), refEq(context, "locale"))).thenReturn(body);

        handler.handle(notification);

        verify(intercomApiClient).postMessage(AdminMessageBuilder.newInstanceWith()
                                                                 .messageType("email")
                                                                 .template("plain")
                                                                 .admin(AdminBuilder.newInstanceWith().id(adminId).build())
                                                                 .subject("Your reward has been transferred to your account")
                                                                 .body(body)
                                                                 .user(UserBuilder.newInstanceWith().email(userEmail).build())
                                                                 .build());
    }
}