package io.fundrequest.platform.intercom.eventhandler;

import io.fundrequest.core.request.BlockchainEventService;
import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.domain.Platform;
import io.fundrequest.core.request.dto.BlockchainEventDto;
import io.fundrequest.core.request.fund.FundService;
import io.fundrequest.core.request.fund.dto.FundDto;
import io.fundrequest.core.request.view.IssueInformationDto;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.notification.dto.RequestFundedNotificationDto;
import io.fundrequest.platform.intercom.IntercomApiClient;
import io.fundrequest.platform.intercom.model.RichLink;
import io.fundrequest.platform.keycloak.KeycloakRepository;
import io.intercom.api.Event;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Component
public class RequestFundedIntercomEventHandler {
    private static final String FUNDED_REQUEST = "Funded request";

    private final FundService fundService;
    private final RequestService requestService;
    private final BlockchainEventService blockchainEventService;
    private final KeycloakRepository keycloakRepository;
    private final IntercomApiClient intercomApiClient;
    private final String etherscanBasePath;
    private final String fundrequestBasepath;

    public RequestFundedIntercomEventHandler(final FundService fundService,
                                             final RequestService requestService,
                                             final BlockchainEventService blockchainEventService,
                                             final KeycloakRepository keycloakRepository,
                                             final IntercomApiClient intercomApiClient,
                                             @Value("${io.fundrequest.etherscan.basepath}") final String etherscanBasePath,
                                             @Value("${io.fundrequest.platform.base-path}") final String fundrequestBasepath) {
        this.fundService = fundService;
        this.requestService = requestService;
        this.blockchainEventService = blockchainEventService;
        this.keycloakRepository = keycloakRepository;
        this.intercomApiClient = intercomApiClient;
        this.etherscanBasePath = etherscanBasePath;
        this.fundrequestBasepath = fundrequestBasepath;
    }

    @Async
    @EventListener
    public void handle(final RequestFundedNotificationDto notification) {
        final FundDto fund = fundService.findOne(notification.getFundId());
        resolveUserEmail(fund).ifPresent(userEmail -> {
            final RequestDto request = requestService.findRequest(notification.getRequestId());
            final IssueInformationDto issueInformation = request.getIssueInformation();
            final String transactionHash = blockchainEventService.findOne(notification.getBlockchainEventId())
                                                                 .map(BlockchainEventDto::getTransactionHash)
                                                                 .orElse("");
            intercomApiClient.postEvent(buildEvent(userEmail,
                                                   notification.getDate(),
                                                   issueInformation.getPlatform(),
                                                   issueInformation.getPlatformId(),
                                                   issueInformation.getTitle(),
                                                   request.getId(),
                                                   transactionHash));
        });
    }

    private Optional<String> resolveUserEmail(final FundDto fund) {
        return Optional.ofNullable(fund.getFunderUserId())
                       .map(userId -> keycloakRepository.getUser(fund.getFunderUserId()))
                       .map(UserRepresentation::getEmail);
    }

    private Event buildEvent(final String userEmail,
                             final LocalDateTime notificationDate,
                             final Platform platform,
                             final String platformId,
                             final String issueTitle,
                             final Long requestId,
                             final String transactionHash) {

        final Event event = new Event().setEventName(FUNDED_REQUEST)
                                       .setCreatedAt(notificationDate.toEpochSecond(ZoneOffset.UTC))
                                       .setEmail(userEmail)
                                       .putMetadata("platform", platform.name())
                                       .putMetadata("platform_id", platformId);
        event.getMetadata().put("issue", RichLink.builder()
                                                 .value(issueTitle)
                                                 .url(String.format("%s/requests/%s", fundrequestBasepath, requestId))
                                                 .build());
        event.getMetadata().put("transaction_hash", RichLink.builder()
                                                            .value(transactionHash)
                                                            .url(String.format("%s/tx/%s", etherscanBasePath, transactionHash))
                                                            .build());
        return event;
    }
}
