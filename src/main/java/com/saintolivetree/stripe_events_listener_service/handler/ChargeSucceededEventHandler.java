package com.saintolivetree.stripe_events_listener_service.handler;

import com.saintolivetree.stripe_events_listener_service.dto.DonationDetails;
import com.saintolivetree.stripe_events_listener_service.model.DonorNotification;
import com.saintolivetree.stripe_events_listener_service.service.*;
import com.stripe.model.Charge;
import com.stripe.model.StripeObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    private EncryptionService encryptionService;

    @Override
    public String getEventType() {
        return "charge.succeeded";
    }

    @Override
    protected void handleStripeObject(StripeObject stripeObject) throws Exception {
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
        byte[] pdf = createPdf(templateVariables);

        String unsubscribeUrl = createUnsubscribeUrl(donorId);

        mailService.sendEmail(
                "velizar.kacharov@gmail.com",
                "Благодарим ви за Вашето дарение",
                String.format("""
                    Екипът на Сдружение Операция: Плюшено Мече Ви благодари за Вашето дарение.
                    За да се отпишете, последвайте този линк %s
                """, unsubscribeUrl),
                pdf);
    }

    private byte[] createPdf(Map<String, Object> templateVariables) {
        Context context = new Context();
        context.setVariables(templateVariables);
        byte[] pdf = pdfService.generatePdf("certificate", context);
        return pdf;
    }

    private String createUnsubscribeUrl(String donorId) {
        String encryptedDonorId = encryptionService.encrypt(donorId);
        return String.format(
                "http://localhost:8080/unsubscribe?d=%s",
                encryptedDonorId);
    }
}
