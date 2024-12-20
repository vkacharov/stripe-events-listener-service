package com.saintolivetree.stripe_events_listener_service.service;

import com.saintolivetree.stripe_events_listener_service.dto.DonationDetails;
import com.saintolivetree.stripe_events_listener_service.exception.TemplateException;
import com.stripe.model.Charge;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Service
public class DonationDetailsService {
    private final Map<String, String> causes = Map.of(
        "Donation in support of association Operation: Teddy Bear".toLowerCase(),
            "менторската програма Скритите Таланти на България",
        "Дарение в подкрепа на Сдружение Операция: Плюшено Мече".toLowerCase(),
            "менторската програма Скритите Таланти на България",
        "Дарение в подкрепа на екипа на сдружение Операция: Плюшено Мече".toLowerCase(),
            "оцеляването на екипа ни",
        "Дарение в подкрепа на Стоян от Скритите таланти на България".toLowerCase(),
            "Стоян, част от менторска програма Скритите Таланти на България",
        "Дарение в подкрепа на Валтер от Скритите таланти на България".toLowerCase(),
            "Валтер, част от менторска програма Скритите Таланти на България",
        "Дарение в подкрепа на Веско от Скритите таланти на България".toLowerCase(),
            "Веско, част от менторска програма Скритите Таланти на България"
    );

    private final Map<String, String> currencies = Map.of(
        "bgn", "лв."
    );

    private final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

    public DonationDetails extractDonationDetails(Charge charge) {
        String donorName = charge.getBillingDetails().getName();
        String donorEmail = charge.getReceiptEmail();
        String currency = extractCurrency(charge.getCurrency());
        BigDecimal amount = formatAmount(charge.getAmount());
        String chargeDescription = charge.getDescription();
        String cause = extractCause(chargeDescription);
        long chargeTimestamp = charge.getCreated() * 1000; // Stripe timestamp is in seconds
        String donationDate = extractDate(chargeTimestamp);
        String certificateDate = extractDate(System.currentTimeMillis());
        String donorId = charge.getCustomer();
        return DonationDetails.builder()
                .donorName(donorName)
                .donorEmail(donorEmail)
                .currency(currency)
                .amount(amount)
                .cause(cause)
                .donationDate(donationDate)
                .certificateDate(certificateDate)
                .donorId(donorId)
                .build();
    }

    private String extractDate(long timestamp) {
        String formattedDate = formatter.format(new Date(timestamp));
        return formattedDate;
    }

    private String extractCurrency(String chargeCurrency) {
        if (currencies.containsKey(chargeCurrency)) {
            return currencies.get(chargeCurrency);
        } else {
            throw new TemplateException("Currency not found: " + chargeCurrency);
        }
    }

    private String extractCause(String chargeDescription) {
        for (String cause : causes.keySet()) {
            if (chargeDescription.toLowerCase().startsWith(cause)) {
                return causes.get(cause);
            }
        }

        throw new TemplateException("Cause not found: " + causes);
    }

    private BigDecimal formatAmount(long amountInCents) {
        BigDecimal centsBigDecimal = new BigDecimal(amountInCents);
        BigDecimal dollars = centsBigDecimal
                .setScale(2, RoundingMode.HALF_UP)
                .divide(new BigDecimal(100));
        return dollars;
    }
}
