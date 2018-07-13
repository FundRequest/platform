package io.fundrequest.platform.intercom;

import io.fundrequest.core.request.BlockchainEventService;
import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.dto.BlockchainEventDto;
import io.fundrequest.core.request.fund.FundService;
import io.fundrequest.core.request.fund.dto.FundDto;
import io.fundrequest.core.request.view.IssueInformationDto;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.request.view.RequestDtoMother;
import io.fundrequest.notification.dto.RequestFundedNotificationDto;
import io.fundrequest.platform.intercom.eventhandler.RequestFundedIntercomEventHandler;
import io.fundrequest.platform.intercom.model.RichLink;
import io.fundrequest.platform.keycloak.KeycloakRepository;
import io.intercom.api.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.representations.idm.UserRepresentation;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RequestFundedIntercomEventHandlerTest {

    private RequestFundedIntercomEventHandler eventHandler;
    private IntercomApiClient intercomApiClient;
    private FundService fundService;
    private KeycloakRepository keycloakRepository;
    private RequestService requestService;
    private BlockchainEventService blockchainEventService;

    @BeforeEach
    void setUp() {
        intercomApiClient = mock(IntercomApiClient.class);
        fundService = mock(FundService.class);
        keycloakRepository = mock(KeycloakRepository.class);
        requestService = mock(RequestService.class);
        blockchainEventService = mock(BlockchainEventService.class);
        eventHandler = new RequestFundedIntercomEventHandler(fundService, requestService, blockchainEventService, keycloakRepository, intercomApiClient, "https://etherscanBasePath", "https://fundRequest");
    }

    @Test
    void handle() {
        final long fundId = 52L;
        final long requestId = 24L;
        final String funderUserId = "sfgd";
        final UserRepresentation userRepresentation = mock(UserRepresentation.class);
        final String funderEmail = "blablabla@blabla.com";
        final FundDto fundDto = FundDto.builder().funderUserId(funderUserId).build();
        final RequestDto request = RequestDtoMother.fundRequestArea51();
        request.setId(requestId);
        final IssueInformationDto issueInformation = request.getIssueInformation();
        final BlockchainEventDto blockchainEvent = BlockchainEventDto.builder().transactionHash("0x24sfd").build();
        final LocalDateTime now = LocalDateTime.now();

        final RequestFundedNotificationDto notification = RequestFundedNotificationDto.builder()
                                                                                      .fundId(fundId)
                                                                                      .date(now)
                                                                                      .requestId(requestId).build();
        final String transactionHash = blockchainEvent.getTransactionHash();
        final Event expectedEvent = new Event().setEventName("Funded request")
                                               .setCreatedAt(now.toEpochSecond(ZoneOffset.UTC))
                                               .setEmail(funderEmail)
                                               .putMetadata("platform", issueInformation.getPlatform().name())
                                               .putMetadata("platform_id", issueInformation.getPlatformId());
        expectedEvent.getMetadata().put("issue", RichLink.builder()
                                                 .value(issueInformation.getTitle())
                                                 .url("https://fundRequest/requests/" + requestId)
                                                 .build());
        expectedEvent.getMetadata().put("transaction_hash", RichLink.builder()
                                                            .value(transactionHash)
                                                            .url("https://etherscanBasePath/tx/" + transactionHash)
                                                            .build());

        when(fundService.findOne(fundId)).thenReturn(fundDto);
        when(requestService.findRequest(requestId)).thenReturn(request);
        when(blockchainEventService.findOne(notification.getBlockchainEventId())).thenReturn(Optional.of(blockchainEvent));
        when(keycloakRepository.getUser(funderUserId)).thenReturn(userRepresentation);
        when(userRepresentation.getEmail()).thenReturn(funderEmail);

        eventHandler.handle(notification);

        verify(intercomApiClient).postEvent(expectedEvent);
    }
}
