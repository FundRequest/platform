package io.fundrequest.core.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class AzraelMessageReceiver {

    public void receiveMessage(String message) {
        System.out.println("Received <" + message + ">");
    }

    @Bean
    Queue queue(@Value("${io.fundrequest.azrael.queueName}") final String queueName) {
        return new Queue(queueName, false);
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
