package iuh.fit.notificationservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${notification.rabbitmq.critical-exchange}")
    private String criticalExchange;

    @Value("${notification.rabbitmq.notification-exchange}")
    private String notificationExchange;

    @Value("${notification.rabbitmq.queues.email-verify}")
    private String emailVerifyQueue;

    @Value("${notification.rabbitmq.queues.email-otp}")
    private String emailOtpQueue;

    @Value("${notification.rabbitmq.queues.email-order}")
    private String emailOrderQueue;

    @Value("${notification.rabbitmq.queues.email-payment}")
    private String emailPaymentQueue;

    @Value("${notification.rabbitmq.routing.email-verify}")
    private String emailVerifyRoutingKey;

    @Value("${notification.rabbitmq.routing.email-otp}")
    private String emailOtpRoutingKey;

    @Value("${notification.rabbitmq.routing.email-order}")
    private String emailOrderRoutingKey;

    @Value("${notification.rabbitmq.routing.email-payment}")
    private String emailPaymentRoutingKey;

    // ── Exchanges ────────────────────────────────────────────────────────────

    @Bean
    public DirectExchange criticalExchange() {
        return ExchangeBuilder.directExchange(criticalExchange).durable(true).build();
    }

    @Bean
    public DirectExchange notificationExchange() {
        return ExchangeBuilder.directExchange(notificationExchange).durable(true).build();
    }

    // ── Queues ───────────────────────────────────────────────────────────────

    @Bean
    public Queue emailVerifyQueue() {
        return QueueBuilder.durable(emailVerifyQueue).build();
    }

    @Bean
    public Queue emailOtpQueue() {
        return QueueBuilder.durable(emailOtpQueue).build();
    }

    @Bean
    public Queue emailOrderQueue() {
        return QueueBuilder.durable(emailOrderQueue).build();
    }

    @Bean
    public Queue emailPaymentQueue() {
        return QueueBuilder.durable(emailPaymentQueue).build();
    }

    // ── Bindings ─────────────────────────────────────────────────────────────

    @Bean
    public Binding bindEmailVerify() {
        return BindingBuilder.bind(emailVerifyQueue()).to(criticalExchange()).with(emailVerifyRoutingKey);
    }

    @Bean
    public Binding bindEmailOtp() {
        return BindingBuilder.bind(emailOtpQueue()).to(criticalExchange()).with(emailOtpRoutingKey);
    }

    @Bean
    public Binding bindEmailOrder() {
        return BindingBuilder.bind(emailOrderQueue()).to(notificationExchange()).with(emailOrderRoutingKey);
    }

    @Bean
    public Binding bindEmailPayment() {
        return BindingBuilder.bind(emailPaymentQueue()).to(notificationExchange()).with(emailPaymentRoutingKey);
    }

    // ── JSON MessageConverter ─────────────────────────────────────────────────

    @Bean
    public MessageConverter jacksonMessageConverter() {
        var converter = new Jackson2JsonMessageConverter(new ObjectMapper().findAndRegisterModules());
        var mapper = new DefaultJackson2JavaTypeMapper();
        mapper.setTrustedPackages("iuh.fit.*");
        converter.setJavaTypeMapper(mapper);
        return converter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jacksonMessageConverter());
        return template;
    }
}
