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
import io.fundrequest.core.request.fund.command.FundsAddedCommand;
import io.fundrequest.core.request.fund.infrastructure.BlockchainEventRepository;
import io.fundrequest.core.request.fund.messaging.dto.ClaimedEthDto;
import io.fundrequest.core.request.fund.messaging.dto.FundedEthDto;
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

    private RequestService requestService;
    private ObjectMapper objectMapper;
    private BlockchainEventRepository blockchainEventRepository;
    private FundService fundService;
    private final PendingFundService pendingFundService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AzraelMessageReceiver.class);

    @Autowired
    public AzraelMessageReceiver(RequestService requestService,
                                 ObjectMapper objectMapper,
                                 BlockchainEventRepository blockchainEventRepository,
                                 FundService fundService, PendingFundService pendingFundService) {
        this.requestService = requestService;
        this.objectMapper = objectMapper;
        this.blockchainEventRepository = blockchainEventRepository;
        this.fundService = fundService;
        this.pendingFundService = pendingFundService;
    }

    @Transactional
    public void receiveFundedMessage(final String message) throws IOException {
        LOGGER.debug("Recieved new message from Azrael: " + message);
        FundedEthDto result = objectMapper.readValue(message, FundedEthDto.class);
        if (!isProcessed(result.getTransactionHash(), result.getLogIndex()) && StringUtils.isNotBlank(result.getPlatformId())) {
            final BlockchainEvent blockchainEvent = blockchainEventRepository.saveAndFlush(new BlockchainEvent(result.getTransactionHash(), result.getLogIndex()));
            final CreateRequestCommand createRequestCommand = new CreateRequestCommand();
            createRequestCommand.setPlatform(getPlatform(result.getPlatform()));
            createRequestCommand.setPlatformId(result.getPlatformId());
            createRequestCommand.setFunds(new BigDecimal(result.getAmount()));
            createRequestCommand.setTimestamp(getTimeStamp(result.getTimestamp()));
            final Long newRequestId = requestService.createRequest(createRequestCommand);
            fundRequest(result, newRequestId, blockchainEvent.getId());
            fundService.clearTotalFundsCache(newRequestId);
            pendingFundService.removePendingFund(result.getTransactionHash());
        }
    }

    private void fundRequest(final FundedEthDto dto, final Long requestId, Long blockchainEventId) {
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
        LOGGER.debug("Recieved new message from Azrael: " + message);
        final ClaimedEthDto result = objectMapper.readValue(message, ClaimedEthDto.class);
        if (!isProcessed(result.getTransactionHash(), result.getLogIndex())) {
            final BlockchainEvent blockchainEvent = blockchainEventRepository.saveAndFlush(new BlockchainEvent(result.getTransactionHash(), result.getLogIndex()));
            final Request request = requestService.requestClaimed(new RequestClaimedCommand(getPlatform(result.getPlatform()),
                                                                                            result.getPlatformId(),
                                                                                            blockchainEvent.getId(),
                                                                                            result.getTransactionHash(),
                                                                                            result.getLogIndex(),
                                                                                            result.getSolver(),
                                                                                            getTimeStamp(result.getTimestamp()),
                                                                                            new BigDecimal(result.getAmount()),
                                                                                            result.getToken()));
            fundService.clearTotalFundsCache(request.getId());
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
