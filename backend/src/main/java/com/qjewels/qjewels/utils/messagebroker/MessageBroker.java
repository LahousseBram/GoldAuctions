package com.qjewels.qjewels.utils.messagebroker;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;

public class MessageBroker {

    private final Dotenv dotenv = Dotenv.load();
    private final String URI = dotenv.get("amqp_url");
    private final int HEARTBEAT = 30;
    private final int CONNECTION_TIMEOUT = 30000;

    private final String QUEUE_NAME = dotenv.get("amqp_queue");
    private final String EXCHANGE_NAME = dotenv.get("amqp_exchange");
    private final String ROUTING_KEY_PATTERN = "gold.*";
    private final String ROUTING_KEY = "gold.auctions";

    public void sendMessage(String message) {
        CachingConnectionFactory connectionFactory = createConnectionFactory();
        RabbitAdmin admin = createRabbitAdmin(connectionFactory);
        setupQueueAndExchange(admin);

        SimpleMessageListenerContainer container = setupListenerContainer(connectionFactory);

        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        sendMessage(template, message);

        waitForProcessing();

        container.stop();
    }

    private CachingConnectionFactory createConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUri(URI);
        connectionFactory.setRequestedHeartBeat(HEARTBEAT);
        connectionFactory.setConnectionTimeout(CONNECTION_TIMEOUT);
        return connectionFactory;
    }

    private RabbitAdmin createRabbitAdmin(CachingConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    private void setupQueueAndExchange(RabbitAdmin admin) {
        Queue queue = new Queue(QUEUE_NAME);
        admin.declareQueue(queue);

        TopicExchange exchange = new TopicExchange(EXCHANGE_NAME);
        admin.declareExchange(exchange);

        admin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_PATTERN));
    }

    private SimpleMessageListenerContainer setupListenerContainer(CachingConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setMessageListener(createMessageListener());
        container.setQueueNames(QUEUE_NAME);
        container.start();
        return container;
    }

    private MessageListenerAdapter createMessageListener() {
        Object listener = new Object() {
            public void handleMessage(String message) {
                System.out.println(message);
            }
        };
        return new MessageListenerAdapter(listener);
    }

    private void sendMessage(RabbitTemplate template, String message) {
        template.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, message);
    }

    private void waitForProcessing() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
