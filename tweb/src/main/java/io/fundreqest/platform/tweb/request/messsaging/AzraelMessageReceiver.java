package io.fundreqest.platform.tweb.request.messsaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.claim.command.RequestClaimedCommand;
import io.fundrequest.core.request.command.CreateRequestCommand;
import io.fundrequest.core.request.domain.BlockchainEvent;
import io.fundrequest.core.request.domain.Platform;
import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.fund.FundService;
import io.fundrequest.core.request.fund.PendingFundService;
import io.fundrequest.core.request.fund.RefundService;
import io.fundrequest.core.request.fund.command.FundsAddedCommand;
import io.fundrequest.core.request.fund.command.RefundProcessedCommand;
import io.fundrequest.core.request.fund.messaging.dto.ClaimedEthDto;
import io.fundrequest.core.request.fund.messaging.dto.FundedEthDto;
import io.fundrequest.core.request.fund.messaging.dto.RefundedEthDto;
import io.fundrequest.core.request.infrastructure.BlockchainEventRepository;
import io.fundrequest.core.request.view.RequestDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class AzraelMessageReceiver {

    private final RequestService requestService;
    private final ObjectMapper objectMapper;
    private final BlockchainEventRepository blockchainEventRepository;
    private final FundService fundService;
    private final PendingFundService pendingFundService;
    private final RefundService refundService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AzraelMessageReceiver.class);

    @Autowired
    public AzraelMessageReceiver(final RequestService requestService,
                                 final ObjectMapper objectMapper,
                                 final BlockchainEventRepository blockchainEventRepository,
                                 final FundService fundService, PendingFundService pendingFundService,
                                 final RefundService refundService) {
        this.requestService = requestService;
        this.objectMapper = objectMapper;
        this.blockchainEventRepository = blockchainEventRepository;
        this.fundService = fundService;
        this.pendingFundService = pendingFundService;
        this.refundService = refundService;
    }

    @Transactional
    public void receiveFundedMessage(final String message) throws IOException {
        LOGGER.debug("Recieved new message from Azrael: %s", message);
        final FundedEthDto incomingMessage = objectMapper.readValue(message, FundedEthDto.class);
        if (!isProcessed(incomingMessage.getTransactionHash(), incomingMessage.getLogIndex()) && StringUtils.isNotBlank(incomingMessage.getPlatformId())) {
            final BlockchainEvent blockchainEvent = blockchainEventRepository.saveAndFlush(new BlockchainEvent(incomingMessage.getTransactionHash(), incomingMessage.getLogIndex()));
            final Long newRequestId = requestService.createRequest(buildCreateRequestCommand(incomingMessage));
            fundRequest(incomingMessage, newRequestId, blockchainEvent.getId());
            fundService.clearTotalFundsCache(newRequestId);
            pendingFundService.removePendingFund(incomingMessage.getTransactionHash());
        }
    }

    private CreateRequestCommand buildCreateRequestCommand(FundedEthDto incomingMessage) {
        final CreateRequestCommand createRequestCommand = new CreateRequestCommand();
        createRequestCommand.setPlatform(getPlatform(incomingMessage.getPlatform()));
        createRequestCommand.setPlatformId(incomingMessage.getPlatformId());
        createRequestCommand.setFunds(new BigDecimal(incomingMessage.getAmount()));
        createRequestCommand.setTimestamp(getTimeStamp(incomingMessage.getTimestamp()));
        return createRequestCommand;
    }

    private void fundRequest(final FundedEthDto dto, final Long requestId, final Long blockchainEventId) {
        fundService.addFunds(FundsAddedCommand.builder()
                                              .requestId(requestId)
                                              .amountInWei(new BigDecimal(dto.getAmount()))
                                              .timestamp(getTimeStamp(dto.getTimestamp()))
                                              .token(dto.getToken())
                                              .funderAddress(dto.getFrom())
                                              .transactionHash(dto.getTransactionHash())
                                              .blockchainEventId(blockchainEventId)
                                              .build());
    }

    private Platform getPlatform(String platform) {
        return Platform.getPlatform(platform).orElseThrow(() -> new RuntimeException("Platform " + platform + " is unknown!"));
    }

    @Transactional
    public void receiveClaimedMessage(final String message) throws IOException {
        LOGGER.debug("Recieved new message from Azrael: %s", message);
        final ClaimedEthDto incommingMessage = objectMapper.readValue(message, ClaimedEthDto.class);
        if (!isProcessed(incommingMessage.getTransactionHash(), incommingMessage.getLogIndex())) {
            final BlockchainEvent blockchainEvent = blockchainEventRepository.saveAndFlush(new BlockchainEvent(incommingMessage.getTransactionHash(), incommingMessage.getLogIndex()));
            final Request request = requestService.requestClaimed(new RequestClaimedCommand(getPlatform(incommingMessage.getPlatform()),
                                                                                            incommingMessage.getPlatformId(),
                                                                                            blockchainEvent.getId(),
                                                                                            incommingMessage.getSolver(),
                                                                                            getTimeStamp(incommingMessage.getTimestamp()),
                                                                                            new BigDecimal(incommingMessage.getAmount()),
                                                                                            incommingMessage.getToken()));
            fundService.clearTotalFundsCache(request.getId());
        }
    }

    @Transactional
    public void receiveRefundedMessage(final String message) throws IOException {
        LOGGER.debug("Recieved new message from Azrael: %s", message);
        final RefundedEthDto incomingMessage = objectMapper.readValue(message, RefundedEthDto.class);
        if (!isProcessed(incomingMessage.getTransactionHash(), incomingMessage.getLogIndex()) && StringUtils.isNotBlank(incomingMessage.getPlatformId())) {
            final BlockchainEvent blockchainEvent = blockchainEventRepository.saveAndFlush(new BlockchainEvent(incomingMessage.getTransactionHash(),
                                                                                                               incomingMessage.getLogIndex()));
            final RequestDto request = requestService.findRequest(getPlatform(incomingMessage.getPlatform()), incomingMessage.getPlatformId());
            refundService.refundProcessed(RefundProcessedCommand.builder()
                                                                .requestId(request.getId())
                                                                .amount(incomingMessage.getAmount())
                                                                .tokenHash(incomingMessage.getToken())
                                                                .funderAddress(incomingMessage.getOwner())
                                                                .blockchainEventId(blockchainEvent.getId())
                                                                .transactionHash(incomingMessage.getTransactionHash())
                                                                .build());
        }
    }

    private LocalDateTime getTimeStamp(final Long time) {
        return time == null ? null : Instant.ofEpochMilli(time)
                                            .atZone(ZoneOffset.UTC)
                                            .toLocalDateTime();
    }

    private boolean isProcessed(final String transactionHash, final String logIndex) {
        return blockchainEventRepository.findByTransactionHashAndLogIndex(transactionHash, logIndex).isPresent();
    }
}
