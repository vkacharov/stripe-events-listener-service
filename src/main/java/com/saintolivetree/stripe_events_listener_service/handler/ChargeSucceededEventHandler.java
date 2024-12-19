package com.saintolivetree.stripe_events_listener_service.handler;

import com.saintolivetree.stripe_events_listener_service.dto.DonationDetails;
import com.saintolivetree.stripe_events_listener_service.model.DonorNotification;
import com.saintolivetree.stripe_events_listener_service.service.*;
import com.stripe.model.Charge;
import com.stripe.model.StripeObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;
import java.util.Optional;

@Service
public class ChargeSucceededEventHandler extends StripeEventHandler {

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

    @Override
    public String getEventType() {
        return "charge.succeeded";
    }

    @Override
    protected void handleStripeObject(StripeObject stripeObject) {
        Charge charge = (Charge) stripeObject;
        DonationDetails donationDetails = donationDetailsService.extractDonationDetails(charge);

        String donorId = donationDetails.getDonorId();
        Optional<DonorNotification.NotificationStatus> status =
                donorNotificationStatusService.getNotificationStatus(donorId);
        if (status.isPresent() && DonorNotification.NotificationStatus.DISABLED.equals(status.get())) {
            // TODO: log metric
            return;
        }

        Map<String, Object> templateVariables = donationDetails.toMap();
        String unsubscribeUrl = mailService.createUnsubscribeUrl(donorId);
        templateVariables.put("unsubscribeUrl", unsubscribeUrl);

        byte[] pdf = createPdf(templateVariables);
        String emailContent = createEmailContent(templateVariables);

        mailService.sendEmail(
                "velizar.kacharov@gmail.com",
                "Благодарим Ви за Вашето дарение",
               emailContent,
                pdf)
        ;
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
}
