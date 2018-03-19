package io.fundrequest.restapi.request.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.claim.command.RequestClaimedCommand;
import io.fundrequest.core.request.command.CreateRequestCommand;
import io.fundrequest.core.request.domain.Platform;
import io.fundrequest.core.request.fund.domain.ProcessedBlockchainEvent;
import io.fundrequest.core.request.fund.infrastructure.ProcessedBlockchainEventRepository;
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
    private ProcessedBlockchainEventRepository processedBlockchainEventRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(AzraelMessageReceiver.class);

    @Autowired
    public AzraelMessageReceiver(RequestService requestService, ObjectMapper objectMapper, ProcessedBlockchainEventRepository processedBlockchainEventRepository) {
        this.requestService = requestService;
        this.objectMapper = objectMapper;
        this.processedBlockchainEventRepository = processedBlockchainEventRepository;
    }

    @Transactional
    public void receiveFundedMessage(String message) throws IOException {
        LOGGER.debug("Recieved new message from Azrael: " + message);
        FundedEthDto result = objectMapper.readValue(message, FundedEthDto.class);
        if (isNewFunding(result)) {
            CreateRequestCommand createRequestCommand = new CreateRequestCommand();
            createRequestCommand.setPlatform(getPlatform(result.getPlatform()));
            createRequestCommand.setPlatformId(result.getPlatformId());
            createRequestCommand.setFunds(new BigDecimal(result.getAmount()));
            createRequestCommand.setTimestamp(getTimeStamp(result.getTimestamp()));
            requestService.createRequest(createRequestCommand);
            processedBlockchainEventRepository.save(new ProcessedBlockchainEvent(result.getTransactionHash()));
        }
    }

    private Platform getPlatform(String platform) {
        return Platform.getPlatform(platform).orElseThrow(() -> new RuntimeException("Platform " + platform + " is unknown!"));
    }

    @Transactional
    public void receiveClaimedMessage(String message) throws IOException {
        LOGGER.debug("Recieved new message from Azrael: " + message);
        ClaimedEthDto result = objectMapper.readValue(message, ClaimedEthDto.class);
        requestService.requestClaimed(new RequestClaimedCommand(
                getPlatform(result.getPlatform()),
                result.getPlatformId(),
                result.getTransactionHash(), result.getSolver(),
                getTimeStamp(result.getTimestamp()),
                new BigDecimal(result.getAmount())));
        processedBlockchainEventRepository.save(new ProcessedBlockchainEvent(result.getTransactionHash()));
    }

    private LocalDateTime getTimeStamp(Long time) {
        return time == null ? null : Instant.ofEpochMilli(time)
                .atZone(ZoneOffset.UTC)
                .toLocalDateTime();
    }

    private boolean isNewFunding(FundedEthDto result) {
        return !processedBlockchainEventRepository.findOne(result.getTransactionHash()).isPresent()
                && StringUtils.isNotBlank(result.getPlatformId());
    }

}
