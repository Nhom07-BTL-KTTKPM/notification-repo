package iuh.fit.notificationservice.email.consumer;

import iuh.fit.notificationservice.email.ResendEmailService;
import iuh.fit.notificationservice.event.EmailVerifyEvent;
import iuh.fit.notificationservice.event.OtpEmailEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class CriticalEmailConsumer {

    private static final Logger log = LoggerFactory.getLogger(CriticalEmailConsumer.class);

    private final ResendEmailService emailService;
    private final TemplateEngine templateEngine;
    private final String frontendBaseUrl;

    public CriticalEmailConsumer(
            ResendEmailService emailService,
            TemplateEngine templateEngine,
            @Value("${notification.mail.frontend-base-url}") String frontendBaseUrl) {
        this.emailService = emailService;
        this.templateEngine = templateEngine;
        this.frontendBaseUrl = frontendBaseUrl;
    }

    @RabbitListener(queues = "${notification.rabbitmq.queues.email-verify}")
    public void handleVerifyEmail(EmailVerifyEvent event) {
        log.info("[CriticalEmail] Received VERIFY event for email={}", event.email());
        try {
            Context ctx = new Context();
            ctx.setVariable("fullName", event.fullName());
            String verificationLink = frontendBaseUrl + "/verify-email?token=" + event.verificationToken();
            ctx.setVariable("verificationLink", verificationLink);

            String html = templateEngine.process("email-verify", ctx);
            String text = String.format(
                    "Xin chào %s,\n\nCảm ơn bạn đã đăng ký tài khoản tại Lumière. Vui lòng xác nhận email bằng cách truy cập liên kết sau:\n%s\n\nNếu bạn không thực hiện yêu cầu này, hãy bỏ qua email này.\n\nTrân trọng,\nLumière Cosmetics",
                    event.fullName(),
                    verificationLink);

            emailService.sendEmail(event.email(), "Xác nhận tài khoản Lumière", html, text);
        } catch (Exception e) {
            log.error("[CriticalEmail] Failed to process VERIFY event for email={}: {}", event.email(), e.getMessage(),
                    e);
        }
    }

    @RabbitListener(queues = "${notification.rabbitmq.queues.email-otp}")
    public void handleOtpEmail(OtpEmailEvent event) {
        log.info("[CriticalEmail] Received OTP event purpose={} for email={}", event.purpose(), event.email());
        try {
            Context ctx = new Context();
            ctx.setVariable("fullName", event.fullName());
            ctx.setVariable("otpCode", event.otpCode());
            ctx.setVariable("purpose", event.purpose());

            String subject = "FORGOT_PASSWORD".equals(event.purpose())
                    ? "Lumière — Xác nhận yêu cầu tài khoản"
                    : "Lumière — Xác nhận tài khoản";

            String html = templateEngine.process("email-otp", ctx);
            String text = String.format(
                    "Xin chào %s,\n\nMã của bạn là: %s\n\nMã này sẽ hết hạn sau 5 phút.\nNếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email này.\n\nTrân trọng,\nLumière Cosmetics",
                    event.fullName(),
                    event.otpCode());

            emailService.sendEmail(event.email(), subject, html, text);
        } catch (Exception e) {
            log.error("[CriticalEmail] Failed to process OTP event for email={}: {}", event.email(), e.getMessage(), e);
        }
    }
}
