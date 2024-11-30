package com.saintolivetree.stripe_events_listener_service.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendEmail(
          String to,
          String subject,
          String text,
          byte[] attachment
    ) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("no-reply@plushenomeche.org");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);

        InputStreamSource inputStreamSource = new ByteArrayResource(attachment);
        helper.addAttachment("donation-certificate.pdf", inputStreamSource, "application/pdf");
        emailSender.send(message);
    }
}
