package iuh.fit.notificationservice.event;

/**
 * Event gửi từ auth-service khi người dùng đăng ký thành công.
 * Routing: critical.exchange → email.verify
 */
public record EmailVerifyEvent(
        String email,
        String fullName,
        String verificationToken
) {}
