package iuh.fit.notificationservice.email;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ResendEmailService {

    private static final Logger log = LoggerFactory.getLogger(ResendEmailService.class);

    private final Resend resend;
    private final String from;

    public ResendEmailService(
            @Value("${notification.mail.resend-api-key}") String apiKey,
            @Value("${notification.mail.from}") String from
    ) {
        this.resend = new Resend(apiKey);
        this.from = from;
    }

    /**
     * Gửi email HTML và Plain Text thông qua Resend API.
     *
     * @param to      địa chỉ người nhận
     * @param subject tiêu đề email
     * @param html    nội dung HTML đã render
     * @param text    nội dung Plain Text (giúp giảm tỷ lệ vào spam)
     */
    public void sendEmail(String to, String subject, String html, String text) {
        try {
            CreateEmailOptions options = CreateEmailOptions.builder()
                    .from(from)
                    .to(to)
                    .subject(subject)
                    .html(html)
                    .text(text)
                    .build();

            var response = resend.emails().send(options);
            log.info("[ResendEmail] Sent to={} subject='{}' messageId={}", to, subject, response.getId());
        } catch (ResendException e) {
            log.error("[ResendEmail] Failed to send email to={} subject='{}': {}", to, subject, e.getMessage(), e);
        }
    }

    // Giữ lại method cũ để tránh breaking changes nếu chưa update hết consumer
    public void sendHtmlEmail(String to, String subject, String html) {
        sendEmail(to, subject, html, null);
    }
}
