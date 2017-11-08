package io.fundrequest.core.request.fund.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fundrequest.core.request.fund.FundService;
import io.fundrequest.core.request.fund.command.AddFundsCommand;
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

    private FundService fundService;
    private ObjectMapper objectMapper;
    private ProcessedBlockchainEventRepository processedBlockchainEventRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(AzraelMessageReceiver.class);

    @Autowired
    public AzraelMessageReceiver(FundService fundService, ObjectMapper objectMapper, ProcessedBlockchainEventRepository processedBlockchainEventRepository) {
        this.fundService = fundService;
        this.objectMapper = objectMapper;
        this.processedBlockchainEventRepository = processedBlockchainEventRepository;
    }

    @Transactional
    public void receiveMessage(String message) throws IOException {
        FundedEthDto result = objectMapper.readValue(message, FundedEthDto.class);
        LOGGER.debug("Recieved new message from Azrael: " + message);
        if (isNewFunding(result)) {
            AddFundsCommand command = new AddFundsCommand();
            command.setAmountInWei(new BigDecimal(result.getAmount()));
            command.setRequestId(new Long(result.getData()));
            fundService.addFunds(result::getUser, command);
            processedBlockchainEventRepository.save(new ProcessedBlockchainEvent(result.getTransactionHash()));
        }
    }

    private boolean isNewFunding(FundedEthDto result) {
        return !processedBlockchainEventRepository.findOne(result.getTransactionHash()).isPresent()
                && StringUtils.isNumeric(result.getData());
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
