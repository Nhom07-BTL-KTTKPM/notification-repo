package iuh.fit.notificationservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@SpringBootTest(properties = {
    "spring.config.import=", // Disables loading external .env file
    
    // Database configuration (H2)
    "SPRING_DATASOURCE_URL=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
    "SPRING_DATASOURCE_USERNAME=sa",
    "SPRING_DATASOURCE_PASSWORD=",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "SPRING_JPA_HIBERNATE_DDL_AUTO=create-drop",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",

    // JWT Configuration
    "AUTH_JWT_ISSUER=auth-service",
    "AUTH_JWT_SECRET=NUQyuZhoBd6u6O6dAP30Rgu5bqv5TdzpKreGhZGExSaHXEQIM3kicw9uptSaJP9Q",

    // Port
    "SERVER_PORT=0",

    // Discovery (Eureka) disabled
    "spring.cloud.discovery.enabled=false",
    "eureka.client.enabled=false",
    "eureka.client.register-with-eureka=false",
    "eureka.client.fetch-registry=false",

    // RabbitMQ Mock/Test properties
    "RABBITMQ_HOST=localhost",
    "RABBITMQ_PORT=5672",
    "RABBITMQ_USERNAME=guest",
    "RABBITMQ_PASSWORD=guest",
    "spring.rabbitmq.listener.simple.auto-startup=false",
    "spring.rabbitmq.listener.direct.auto-startup=false",

    // Notification properties
    "notification.mail.resend-api-key=mock-key",
    "notification.mail.from=mock@test.com",
    "notification.mail.frontend-base-url=http://localhost:3000",
    "NOTIFICATION_RABBITMQ_CRITICAL_EXCHANGE=critical.exchange",
    "NOTIFICATION_RABBITMQ_NOTIFICATION_EXCHANGE=notification.exchange",
    "NOTIFICATION_RABBITMQ_ROUTING_EMAIL_VERIFY=email.verify",
    "NOTIFICATION_RABBITMQ_ROUTING_EMAIL_OTP=email.otp",
    "NOTIFICATION_RABBITMQ_ROUTING_EMAIL_ORDER=email.order",
    "NOTIFICATION_RABBITMQ_ROUTING_EMAIL_PAYMENT=email.payment",
    "NOTIFICATION_RABBITMQ_QUEUE_EMAIL_VERIFY=email.verify.queue",
    "NOTIFICATION_RABBITMQ_QUEUE_EMAIL_OTP=email.otp.queue",
    "NOTIFICATION_RABBITMQ_QUEUE_EMAIL_ORDER=email.order.queue",
    "NOTIFICATION_RABBITMQ_QUEUE_EMAIL_PAYMENT=email.payment.queue"
})
@ActiveProfiles("test")
class NotificationServiceApplicationTests {

    @MockitoBean
    private ConnectionFactory connectionFactory;

    @MockitoBean
    private RabbitTemplate rabbitTemplate;

    @Test
    void contextLoads() {
    }

}
