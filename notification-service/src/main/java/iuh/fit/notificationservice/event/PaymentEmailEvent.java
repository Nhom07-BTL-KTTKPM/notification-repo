package iuh.fit.notificationservice.event;

import java.math.BigDecimal;

/**
 * Event gửi thông báo kết quả thanh toán.
 * Routing: notification.exchange → email.payment
 */
public record PaymentEmailEvent(
        String email,
        String fullName,
        String orderCode,
        /** "PAID" hoặc "FAILED" */
        String paymentStatus,
        BigDecimal amount
) {}
