package com.saintolivetree.stripe_events_listener_service.handler;

import com.saintolivetree.stripe_events_listener_service.dto.DonationDetails;
import com.saintolivetree.stripe_events_listener_service.model.DonorNotification;
import com.saintolivetree.stripe_events_listener_service.service.*;
import com.saintolivetree.stripe_events_listener_service.service.metrics.MetricsService;
import com.saintolivetree.stripe_events_listener_service.web.advice.WebhookExceptionHandler;
import com.stripe.model.Charge;
import com.stripe.model.StripeObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;
import java.util.Optional;

@Service
public class ChargeSucceededEventHandler extends StripeEventHandler {

    @Value("${public.url}")
    private String publicUrl;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private MailService mailService;

    @Autowired
    private DonationDetailsService donationDetailsService;

    @Autowired
    private DonorNotificationStatusService donorNotificationStatusService;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private MetricsService metricsService;

    private static final Logger logger = LoggerFactory.getLogger(ChargeSucceededEventHandler.class);

    @Override
    public String getEventType() {
        return "charge.succeeded";
    }

    @Override
    protected void handleStripeObject(StripeObject stripeObject) {
        Charge charge = (Charge) stripeObject;
        DonationDetails donationDetails = donationDetailsService.extractDonationDetails(charge);

        String donorId = donationDetails.getDonorId();
        String encryptedDonorId = encryptionService.encrypt(donorId);

        logger.info("Received a charge.succeeded event for Donor with id {}.", encryptedDonorId);

        Optional<DonorNotification.NotificationStatus> status =
                donorNotificationStatusService.getNotificationStatus(donorId);
        if (status.isPresent() && DonorNotification.NotificationStatus.DISABLED.equals(status.get())) {
            logger.info("Donor with id {} has unsubscribed. Not sending an email.", encryptedDonorId);
            metricsService.incrementMetric("event.UnsubscribeEventSkipped");
            return;
        }

        Map<String, Object> templateVariables = donationDetails.toMap();
        String unsubscribeUrl = createUnsubscribeUrl(encryptedDonorId);
        templateVariables.put("unsubscribeUrl", unsubscribeUrl);

        byte[] pdf = createPdf(templateVariables);
        String emailContent = createEmailContent(templateVariables);

        mailService.sendEmail(
                "velizar.kacharov@gmail.com",
                "Благодарим Ви за Вашето дарение",
               emailContent,
                pdf);

        logger.info("Successfully sent an email to Donor with id {}.", encryptedDonorId);
        metricsService.incrementMetric("event.CertificateEmailSucceeded");
    }

    private byte[] createPdf(Map<String, Object> templateVariables) {
        Context context = new Context();
        context.setVariables(templateVariables);
        byte[] pdf = pdfService.generatePdf("certificate", context);
        return pdf;
    }

    private String createEmailContent(Map<String, Object> templateVariables) {
        Context context = new Context();
        context.setVariables(templateVariables);
        String html = templateEngine.process("email", context);
        return html;
    }


    public String createUnsubscribeUrl(String encryptedDonorId) {
        return String.format(
                publicUrl + "unsubscribe?d=%s",
                encryptedDonorId);
    }
}
