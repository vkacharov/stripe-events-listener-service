package com.saintolivetree.stripe_events_listener_service.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Builder
@Getter
public class DonationDetails {
    private String donorName;
    private String donorEmail;
    private String currency;
    private BigDecimal amount;
    private String cause;
    private String donationDate;
    private String certificateDate;
    private String donorId;

    public Map<String, Object> toMap() {
        return new HashMap<>(Map.of(
                "donorName", donorName,
                "donorEmail", donorEmail,
                "currency", currency,
                "amount", amount,
                "cause", cause,
                "donationDate", donationDate,
                "certificateDate", certificateDate,
                "donorId", donorId
        ));
    }
}
