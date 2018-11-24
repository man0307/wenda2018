package com.batman.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author manchaoyang
 * 2018/11/23
 */
@Configuration
public class RabbitMQConfig {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final String EXCHANGE_ALL_EVENT = "mq-exchange-all-event";


    public static final String QUEUE_ADD_QUESTION = "queue-add-question";


    public static final String ROUTINGKEY_ADD_QUESTION = "routingKey-add-question";


    public static final String QUEUE_FEED_EVENT = "queue-feed-event";


    public static final String ROUTINGKEY_FEED_EVENT = "routingKey-feed-event";


    public static final String QUEUE_FOLLOW_EVENT = "queue-follow-event";


    public static final String ROUTINGKEY_FOLLOW_EVENT = "routingKey-follow-event";


    public static final String QUEUE_LIKE_EVENT = "queue-like-event";


    public static final String ROUTINGKEY_LIKE_EVENT = "routingKey-like-event";


    public static final String QUEUE_LOGIN_EXCEPTION = "queue-login-exception-event";


    public static final String ROUTINGKEY_LOGIN_EXCEPTION = "routingKey-login-exception-event";


    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;


    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host, port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setPublisherConfirms(true);
        return connectionFactory;
    }


    /**
     * 针对消费者配置
     * 1. 设置交换机类型
     * 2. 将队列绑定到交换机
     * FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念
     * HeadersExchange ：通过添加属性key-value匹配
     * DirectExchange:按照routingkey分发到指定队列
     * TopicExchange:多关键字匹配
     *
     * @return
     */

    @Bean
    public DirectExchange exchangeAllEvent() {
        return new DirectExchange(EXCHANGE_ALL_EVENT);
    }

    @Bean
    public Queue queueAddQuestion() {
        return new Queue(QUEUE_ADD_QUESTION, true);
    }


    @Bean
    public Binding bindingAddQuestion() {
        return BindingBuilder.bind(queueAddQuestion()).to(exchangeAllEvent()).with(RabbitMQConfig.ROUTINGKEY_ADD_QUESTION);
    }

    @Bean
    public Queue queueFeedEvent() {
        return new Queue(QUEUE_FEED_EVENT, true);
    }


    @Bean
    public Binding bindingFeedEvent() {
        return BindingBuilder.bind(queueFeedEvent()).to(exchangeAllEvent()).with(RabbitMQConfig.ROUTINGKEY_FEED_EVENT);
    }

    @Bean
    public Queue queueFollowEvent() {
        return new Queue(QUEUE_FOLLOW_EVENT, true);
    }


    @Bean
    public Binding bindingFollowEvent() {
        return BindingBuilder.bind(queueFollowEvent()).to(exchangeAllEvent()).with(RabbitMQConfig.ROUTINGKEY_FOLLOW_EVENT);
    }

    @Bean
    public Queue queueLikeEvent() {
        return new Queue(QUEUE_LIKE_EVENT, true);
    }


    @Bean
    public Binding bindingLikeEvent() {
        return BindingBuilder.bind(queueLikeEvent()).to(exchangeAllEvent()).with(RabbitMQConfig.ROUTINGKEY_LIKE_EVENT);
    }

    @Bean
    public Queue queueLoginExceptionEvent() {
        return new Queue(QUEUE_FOLLOW_EVENT, true);
    }


    @Bean
    public Binding bindingLoginExceptionEvent() {
        return BindingBuilder.bind(queueLoginExceptionEvent()).to(exchangeAllEvent()).with(RabbitMQConfig.ROUTINGKEY_LOGIN_EXCEPTION);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        return factory;
    }
}
