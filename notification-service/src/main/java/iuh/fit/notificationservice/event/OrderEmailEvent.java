package iuh.fit.notificationservice.event;

import java.math.BigDecimal;

/**
 * Event gửi thông báo đơn hàng.
 * Routing: notification.exchange → email.order
 */
public record OrderEmailEvent(
        String email,
        String fullName,
        String orderCode,
        /** Ví dụ: "CONFIRMED", "SHIPPING", "DELIVERED", "CANCELLED" */
        String status,
        BigDecimal totalAmount,
        String updatedAt
) {}
