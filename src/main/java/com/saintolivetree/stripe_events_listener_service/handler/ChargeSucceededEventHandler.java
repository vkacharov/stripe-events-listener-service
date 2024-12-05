package com.saintolivetree.stripe_events_listener_service.handler;

import com.saintolivetree.stripe_events_listener_service.dto.DonationDetails;
import com.saintolivetree.stripe_events_listener_service.service.DonationDetailsService;
import com.saintolivetree.stripe_events_listener_service.service.MailService;
import com.saintolivetree.stripe_events_listener_service.service.PdfService;
import com.stripe.model.Charge;
import com.stripe.model.StripeObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
public class ChargeSucceededEventHandler extends StripeEventHandler {

    @Autowired
    private PdfService pdfService;

    @Autowired
    MailService mailService;

    @Autowired
    DonationDetailsService donationDetailsService;

    @Override
    public String getEventType() {
        return "charge.succeeded";
    }

    @Override
    protected void handleStripeObject(StripeObject stripeObject) throws Exception {
        Charge charge = (Charge) stripeObject;
        DonationDetails donationDetails = donationDetailsService.extractDonationDetails(charge);
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
