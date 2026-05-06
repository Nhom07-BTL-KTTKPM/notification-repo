package iuh.fit.notificationservice.email.consumer;

import iuh.fit.notificationservice.email.ResendEmailService;
import iuh.fit.notificationservice.event.OrderEmailEvent;
import iuh.fit.notificationservice.event.PaymentEmailEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class NotificationEmailConsumer {

    private static final Logger log = LoggerFactory.getLogger(NotificationEmailConsumer.class);

    private final ResendEmailService emailService;
    private final TemplateEngine templateEngine;

    public NotificationEmailConsumer(ResendEmailService emailService, TemplateEngine templateEngine) {
        this.emailService = emailService;
        this.templateEngine = templateEngine;
    }

    @RabbitListener(queues = "${notification.rabbitmq.queues.email-order}")
    public void handleOrderEmail(OrderEmailEvent event) {
        log.info("[NotificationEmail] Received ORDER event orderCode={} status={}", event.orderCode(), event.status());
        try {
            Context ctx = new Context();
            ctx.setVariable("fullName", event.fullName());
            ctx.setVariable("orderCode", event.orderCode());
            ctx.setVariable("status", event.status());
            ctx.setVariable("totalAmount", event.totalAmount());
            ctx.setVariable("updatedAt", event.updatedAt());

            String html = templateEngine.process("email-order", ctx);
            String text = String.format(
                    "Xin chào %s,\n\nĐơn hàng %s của bạn hiện đang ở trạng thái: %s.\nTổng số tiền: %s.\n\nCảm ơn bạn đã mua sắm tại Lumière Cosmetics.",
                    event.fullName(), event.orderCode(), event.status(), event.totalAmount()
            );

            emailService.sendEmail(event.email(),
                    "Cập nhật đơn hàng " + event.orderCode() + " — Lumière", html, text);
        } catch (Exception e) {
            log.error("[NotificationEmail] Failed to process ORDER event for orderCode={}: {}",
                    event.orderCode(), e.getMessage(), e);
        }
    }

    @RabbitListener(queues = "${notification.rabbitmq.queues.email-payment}")
    public void handlePaymentEmail(PaymentEmailEvent event) {
        log.info("[NotificationEmail] Received PAYMENT event orderCode={} status={}", event.orderCode(), event.paymentStatus());
        try {
            Context ctx = new Context();
            ctx.setVariable("fullName", event.fullName());
            ctx.setVariable("orderCode", event.orderCode());
            ctx.setVariable("paymentStatus", event.paymentStatus());
            ctx.setVariable("amount", event.amount());

            String subject = "PAID".equals(event.paymentStatus())
                    ? "Thanh toán thành công đơn hàng " + event.orderCode()
                    : "Thông báo thanh toán đơn hàng " + event.orderCode();

            String html = templateEngine.process("email-payment", ctx);
            String text = String.format(
                    "Xin chào %s,\n\nThông báo thanh toán cho đơn hàng %s.\nSố tiền: %s.\nTrạng thái: %s.\n\nCảm ơn bạn đã tin tưởng Lumière Cosmetics.",
                    event.fullName(), event.orderCode(), event.amount(), event.paymentStatus()
            );

            emailService.sendEmail(event.email(), subject, html, text);
        } catch (Exception e) {
            log.error("[NotificationEmail] Failed to process PAYMENT event for orderCode={}: {}",
                    event.orderCode(), e.getMessage(), e);
        }
    }
}
