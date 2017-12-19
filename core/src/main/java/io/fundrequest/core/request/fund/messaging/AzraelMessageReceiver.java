package io.fundrequest.core.request.fund.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.command.CreateRequestCommand;
import io.fundrequest.core.request.domain.Platform;
import io.fundrequest.core.request.fund.domain.ProcessedBlockchainEvent;
import io.fundrequest.core.request.fund.infrastructure.ProcessedBlockchainEventRepository;
import io.fundrequest.core.request.fund.messaging.dto.FundedEthDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;

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
    public void receiveMessage(String message) throws IOException {
        LOGGER.debug("Recieved new message from Azrael: " + message);
        FundedEthDto result = objectMapper.readValue(message, FundedEthDto.class);
        if (isNewFunding(result)) {
            CreateRequestCommand createRequestCommand = new CreateRequestCommand();
            createRequestCommand.setPlatform(Platform.getPlatform(result.getPlatform()).orElseThrow(() -> new RuntimeException("Platform " + result.getPlatform() + " is unknown!")));
            createRequestCommand.setPlatformId(result.getPlatformId());
            createRequestCommand.setFunds(new BigDecimal(result.getAmount()));
            createRequestCommand.setIssueLink(result.getUrl());
            requestService.createRequest(createRequestCommand);
            processedBlockchainEventRepository.save(new ProcessedBlockchainEvent(result.getTransactionHash()));
        }
    }

    private boolean isNewFunding(FundedEthDto result) {
        return !processedBlockchainEventRepository.findOne(result.getTransactionHash()).isPresent()
                && StringUtils.isNumeric(result.getPlatformId());
    }

    @Bean
    Queue queue(@Value("${io.fundrequest.azrael.queueName}") final String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange("azrael-exchange");
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange, @Value("${io.fundrequest.azrael.queueName}") final String queueName) {
        return BindingBuilder.bind(queue).to(exchange).with(queueName);
    }
}
