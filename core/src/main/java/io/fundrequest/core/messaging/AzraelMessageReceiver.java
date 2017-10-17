package io.fundrequest.core.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fundrequest.core.messaging.dto.FundedEthDto;
import io.fundrequest.core.request.fund.FundService;
import io.fundrequest.core.request.fund.command.AddFundsCommand;
import org.apache.commons.lang3.StringUtils;
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
import java.math.BigInteger;

@Component
public class AzraelMessageReceiver {

    private FundService fundService;
    private ObjectMapper objectMapper;

    @Autowired
    public AzraelMessageReceiver(FundService fundService, ObjectMapper objectMapper) {
        this.fundService = fundService;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void receiveMessage(String message) throws IOException {
        FundedEthDto result = objectMapper.readValue(message, FundedEthDto.class);
        if (StringUtils.isNumeric(result.getData())) {
            AddFundsCommand command = new AddFundsCommand();
            command.setAmountInWei(new BigInteger(result.getAmount()));
            command.setRequestId(new Long(result.getData()));
            fundService.addFunds(result::getUser, command);
        }
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
