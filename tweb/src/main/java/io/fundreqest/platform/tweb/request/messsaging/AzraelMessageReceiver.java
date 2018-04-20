package io.fundreqest.platform.tweb.request.messsaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.claim.command.RequestClaimedCommand;
import io.fundrequest.core.request.command.CreateRequestCommand;
import io.fundrequest.core.request.domain.Platform;
import io.fundrequest.core.request.fund.FundService;
import io.fundrequest.core.request.fund.command.FundsAddedCommand;
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
    private FundService fundService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AzraelMessageReceiver.class);

    @Autowired
    public AzraelMessageReceiver(RequestService requestService,
                                 ObjectMapper objectMapper,
                                 ProcessedBlockchainEventRepository processedBlockchainEventRepository,
                                 FundService fundService) {
        this.requestService = requestService;
        this.objectMapper = objectMapper;
        this.processedBlockchainEventRepository = processedBlockchainEventRepository;
        this.fundService = fundService;
    }

    @Transactional
    public void receiveFundedMessage(String message) throws IOException {
        LOGGER.debug("Recieved new message from Azrael: " + message);
        FundedEthDto result = objectMapper.readValue(message, FundedEthDto.class);
        if (isNotProcessed(result)) {
            CreateRequestCommand createRequestCommand = new CreateRequestCommand();
            createRequestCommand.setPlatform(getPlatform(result.getPlatform()));
            createRequestCommand.setPlatformId(result.getPlatformId());
            createRequestCommand.setFunds(new BigDecimal(result.getAmount()));
            createRequestCommand.setTimestamp(getTimeStamp(result.getTimestamp()));
            Long newRequestId = requestService.createRequest(createRequestCommand);
            fundRequest(result, newRequestId);
            processedBlockchainEventRepository.save(new ProcessedBlockchainEvent(result.getTransactionHash()));
            fundService.clearTotalFundsCache(newRequestId);
            fundService.removePendingFund(result.getTransactionHash());
        }
    }

    private void fundRequest(FundedEthDto dto, Long requestId) {
        FundsAddedCommand fundsCommand =
                FundsAddedCommand.builder()
                                 .requestId(requestId)
                                 .amountInWei(new BigDecimal(dto.getAmount()))
                                 .timestamp(getTimeStamp(dto.getTimestamp()))
                                 .token(dto.getToken())
                                 .funderAddress(dto.getFrom())
                                 .transactionId(dto.getTransactionHash())
                                 .build();
        fundService.addFunds(fundsCommand);
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

    private boolean isNotProcessed(FundedEthDto result) {
        return !processedBlockchainEventRepository.findOne(result.getTransactionHash()).isPresent()
               && StringUtils.isNotBlank(result.getPlatformId());
    }

}
