package io.fundrequest.restapi.config;

import io.fundrequest.restapi.request.messaging.AzraelMessageReceiver;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzraelJmsListenerConfig {
    @Bean
    SimpleMessageListenerContainer fundedContainer(ConnectionFactory connectionFactory,
                                                   MessageListenerAdapter fundedListenerAdapter,
                                                   @Value("${io.fundrequest.azrael.queue.fund}") final String queueName) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(fundedListenerAdapter);
        return container;
    }

    @Bean
    SimpleMessageListenerContainer claimedContainer(ConnectionFactory connectionFactory,
                                                    MessageListenerAdapter claimedListenerAdapter,
                                                    @Value("${io.fundrequest.azrael.queue.claim}") final String queueName) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(claimedListenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter fundedListenerAdapter(AzraelMessageReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveFundedMessage");
    }

    @Bean
    MessageListenerAdapter claimedListenerAdapter(AzraelMessageReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveClaimedMessage");
    }

    @Bean
    Queue fundQueue(@Value("${io.fundrequest.azrael.queue.fund}") final String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    Binding fundBinding(Queue fundQueue, TopicExchange exchange, @Value("${io.fundrequest.azrael.queue.fund}") final String queueName) {
        return BindingBuilder.bind(fundQueue).to(exchange).with(queueName);
    }

    @Bean
    Queue claimQueue(@Value("${io.fundrequest.azrael.queue.claim}") final String queueName) {
        return new Queue(queueName, true);
    }


    @Bean
    Binding claimBinding(Queue claimQueue, TopicExchange exchange, @Value("${io.fundrequest.azrael.queue.claim}") final String queueName) {
        return BindingBuilder.bind(claimQueue).to(exchange).with(queueName);
    }
}
