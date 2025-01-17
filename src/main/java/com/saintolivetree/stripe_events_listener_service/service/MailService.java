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

    @Value("${admin.email}")
    private String adminEmail;

    @Autowired
    private JavaMailSender emailSender;

    public void sendEmail(
            String recipientEmail,
            String subject,
            String text,
            byte[] attachment
    ) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("no-reply@plushenomeche.org");
            helper.setTo(recipientEmail);
            helper.setSubject(subject);
            helper.setText(text, true);
            helper.setBcc(adminEmail);

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
}
