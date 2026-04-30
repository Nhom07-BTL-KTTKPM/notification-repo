package iuh.fit.notificationservice.event;

/**
 * Event gửi OTP cho Quên mật khẩu hoặc Đổi mật khẩu.
 * Routing: critical.exchange → email.otp
 */
public record OtpEmailEvent(
        String email,
        String fullName,
        String otpCode,
        /** "FORGOT_PASSWORD" hoặc "CHANGE_PASSWORD" */
        String purpose
) {}
