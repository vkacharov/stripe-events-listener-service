package com.saintolivetree.stripe_events_listener_service.service;

import com.saintolivetree.stripe_events_listener_service.exception.MailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    @Value("${public.url}")
    private String publicUrl;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private EncryptionService encryptionService;

    public void sendEmail(
          String to,
          String subject,
          String text,
          byte[] attachment
    ) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("no-reply@plushenomeche.org");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);

            ClassPathResource imageResource = new ClassPathResource("static/images/book.png");
            helper.addInline("BookLinkImage", imageResource);
            if (null != attachment) {
                InputStreamSource inputStreamSource = new ByteArrayResource(attachment);
                helper.addAttachment("donation-certificate.pdf", inputStreamSource, "application/pdf");
            }
            emailSender.send(message);
        } catch (MessagingException m) {
            throw new MailException("Failed to send email.", m);
        }
    }

    public String createUnsubscribeUrl(String donorId) {
        String encryptedDonorId = encryptionService.encrypt(donorId);
        return String.format(
                publicUrl + "unsubscribe?d=%s",
                encryptedDonorId);
    }
}
