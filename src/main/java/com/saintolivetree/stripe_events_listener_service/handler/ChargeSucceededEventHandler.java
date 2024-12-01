package com.saintolivetree.stripe_events_listener_service.handler;

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

@Service
public class ChargeSucceededEventHandler extends StripeEventHandler {

    @Autowired
    private PdfService pdfService;

    @Autowired
    MailService mailService;

    @Override
    public String getEventType() {
        return "charge.succeeded";
    }

    @Override
    protected void handleStripeObject(StripeObject stripeObject) throws Exception {
        Charge charge = (Charge) stripeObject;
        String name = charge.getBillingDetails().getName();
        BigDecimal amount = formatAmount(charge.getAmount());

        Map<String, Object> templateVariables = Map.of(
                "name", name,
                "amount", amount.doubleValue()
        );
        byte[] pdf = createPdf(templateVariables);
        mailService.sendEmail(
                "velizar.kacharov@gmail.com",
                "Test email",
                "This is a test email from Plushenomeche",
                pdf);
    }

    private BigDecimal formatAmount(long amountInCents) {
        BigDecimal centsBigDecimal = new BigDecimal(amountInCents);
        BigDecimal dollars = centsBigDecimal
                .setScale(2, RoundingMode.HALF_UP)
                .divide(new BigDecimal(100));
        return dollars;
    }
    private byte[] createPdf(Map<String, Object> templateVariables) {
        Context context = new Context();
        context.setVariables(templateVariables);
        byte[] pdf = pdfService.generatePdf("certificate", context);
        return pdf;
    }
}
