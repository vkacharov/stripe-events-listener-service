package com.saintolivetree.stripe_events_listener_service.handler;

import com.saintolivetree.stripe_events_listener_service.dto.DonationDetails;
import com.saintolivetree.stripe_events_listener_service.model.DonorNotification;
import com.saintolivetree.stripe_events_listener_service.service.DonationDetailsService;
import com.saintolivetree.stripe_events_listener_service.service.DonorNotificationStatusService;
import com.saintolivetree.stripe_events_listener_service.service.MailService;
import com.saintolivetree.stripe_events_listener_service.service.PdfService;
import com.stripe.model.Charge;
import com.stripe.model.StripeObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    @Override
    public String getEventType() {
        return "charge.succeeded";
    }

    @Override
    protected void handleStripeObject(StripeObject stripeObject) throws Exception {
        Charge charge = (Charge) stripeObject;
        DonationDetails donationDetails = donationDetailsService.extractDonationDetails(charge);
        Optional<DonorNotification.NotificationStatus> status =
                donorNotificationStatusService.getNotificationStatus(donationDetails.getDonorId());

        if (status.isPresent() && DonorNotification.NotificationStatus.DISABLED.equals(status.get())) {
            // TODO: log metric
            return;
        }
        
        Map<String, Object> templateVariables = donationDetails.toMap();
        byte[] pdf = createPdf(templateVariables);
        mailService.sendEmail(
                "velizar.kacharov@gmail.com",
                "Благодарим ви за Вашето дарение",
                "Екипът на Сдружение Операция: Плюшено Мече Ви благодари за Вашето дарение.",
                pdf);
    }

    private byte[] createPdf(Map<String, Object> templateVariables) {
        Context context = new Context();
        context.setVariables(templateVariables);
        byte[] pdf = pdfService.generatePdf("certificate", context);
        return pdf;
    }
}
