package io.fundreqest.platform.tweb.config;

import io.fundreqest.platform.tweb.request.messsaging.AzraelMessageReceiver;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AzraelJmsListenerConfig {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Bean
    TopicExchange exchange() {
        return new TopicExchange("azrael-exchange");
    }

    @Bean
    SimpleMessageListenerContainer fundedContainer(final ConnectionFactory connectionFactory,
                                                   final MessageListenerAdapter fundedListenerAdapter,
                                                   @Value("${io.fundrequest.azrael.queue.fund}") final String queueName) {
        return createContainer(connectionFactory, fundedListenerAdapter, queueName);
    }

    @Bean
    SimpleMessageListenerContainer claimedContainer(final ConnectionFactory connectionFactory,
                                                    final MessageListenerAdapter claimedListenerAdapter,
                                                    @Value("${io.fundrequest.azrael.queue.claim}") final String queueName) {
        return createContainer(connectionFactory, claimedListenerAdapter, queueName);
    }

    @Bean
    SimpleMessageListenerContainer refundedContainer(final ConnectionFactory connectionFactory,
                                                     final MessageListenerAdapter refundedListenerAdapter,
                                                     @Value("${io.fundrequest.azrael.queue.refund}") final String queueName) {
        return createContainer(connectionFactory, refundedListenerAdapter, queueName);
    }

    private SimpleMessageListenerContainer createContainer(final ConnectionFactory connectionFactory, final MessageListenerAdapter messageListenerAdapter, final String queueName) {
        final SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setDefaultRequeueRejected(false);
        container.setMessageListener(messageListenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter fundedListenerAdapter(final AzraelMessageReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveFundedMessage");
    }

    @Bean
    MessageListenerAdapter claimedListenerAdapter(final AzraelMessageReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveClaimedMessage");
    }

    @Bean
    MessageListenerAdapter refundedListenerAdapter(final AzraelMessageReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveRefundedMessage");
    }

    @Bean
    public RabbitAdmin rabbitAdmin() {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    Queue fundQueue(@Value("${io.fundrequest.azrael.queue.fund}") final String queueName) {
        return declareQueue(queueName);
    }

    @Bean
    public Queue fundQueueDlQ(@Value("${io.fundrequest.azrael.queue.fund}") final String queueName) {
        return declareDLQ(queueName);
    }

    @Bean
    Binding fundBinding(final Queue fundQueue, final TopicExchange exchange, @Value("${io.fundrequest.azrael.queue.fund}") final String queueName) {
        return BindingBuilder.bind(fundQueue).to(exchange).with(queueName);
    }

    @Bean
    Queue claimQueue(@Value("${io.fundrequest.azrael.queue.claim}") final String queueName) {
        return declareQueue(queueName);
    }

    @Bean
    Queue claimQueueDlQ(@Value("${io.fundrequest.azrael.queue.claim}") final String queueName) {
        return declareDLQ(queueName);
    }

    @Bean
    Binding claimBinding(final Queue claimQueue, final TopicExchange exchange, @Value("${io.fundrequest.azrael.queue.claim}") final String queueName) {
        return BindingBuilder.bind(claimQueue).to(exchange).with(queueName);
    }

    @Bean
    Queue refundQueue(@Value("${io.fundrequest.azrael.queue.refund}") final String queueName) {
        return declareQueue(queueName);
    }

    @Bean
    Queue refundQueueDlQ(@Value("${io.fundrequest.azrael.queue.refund}") final String queueName) {
        return declareDLQ(queueName);
    }

    @Bean
    Binding refundBinding(final Queue refundQueue, final TopicExchange exchange, @Value("${io.fundrequest.azrael.queue.refund}") final String queueName) {
        return BindingBuilder.bind(refundQueue).to(exchange).with(queueName);
    }

    private Queue declareQueue(final String queueName) {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "");
        arguments.put("x-dead-letter-routing-key", queueName + ".dlq");
        return new Queue(queueName, true, false, false, arguments);
    }

    private Queue declareDLQ(final String queueName) {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-queue-mode", "lazy");
        Queue queue = new Queue(queueName + ".dlq", true, false, false, arguments);
        queue.setAdminsThatShouldDeclare(rabbitAdmin());
        rabbitAdmin().declareQueue(queue);
        return queue;
    }
}
