package com.batman.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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

    public static final String EXCHANGE_ADD_QUESTION = "mq-exchange_add_question";


    public static final String QUEUE_ADD_QUESTION = "QUEUE_ADD_QUESTION";

    public static final String ROUTINGKEY_ADD_QUESTION = "add_question-routingKey";

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

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        return template;
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
    public DirectExchange addQuestionExchange() {
        return new DirectExchange(EXCHANGE_ADD_QUESTION);
    }

    @Bean
    public Queue queueAddQuestion() {
        return new Queue(QUEUE_ADD_QUESTION, true);
    }


    @Bean
    public Binding bindingAddQuestion() {
        return BindingBuilder.bind(queueAddQuestion()).to(addQuestionExchange()).with(RabbitMQConfig.ROUTINGKEY_ADD_QUESTION);
    }

}
