package iuh.fit.notificationservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@SpringBootTest(properties = {
    "SPRING_DATASOURCE_URL=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
    "SPRING_DATASOURCE_USERNAME=sa",
    "SPRING_DATASOURCE_PASSWORD=",
    "SPRING_JPA_HIBERNATE_DDL_AUTO=create-drop",
    "AUTH_JWT_ISSUER=auth-service",
    "AUTH_JWT_SECRET=test-secret-32-bytes-long-000000",
    "RESEND_API_KEY=test-resend-key",
    "MAIL_FROM=no-reply@example.com",
    "FRONTEND_BASE_URL=http://localhost:5173",
    "spring.cloud.discovery.enabled=false",
    "eureka.client.enabled=false",
    "eureka.client.register-with-eureka=false",
    "eureka.client.fetch-registry=false"
})
@ActiveProfiles("test")
class NotificationServiceApplicationTests {

    @MockitoBean
    private RabbitTemplate rabbitTemplate;

    @Test
    void contextLoads() {
    }

}
