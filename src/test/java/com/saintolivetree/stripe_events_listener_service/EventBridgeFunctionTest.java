package com.saintolivetree.stripe_events_listener_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EventBridgeFunctionTest {
    @Autowired
    EventBridgeFunction ebf;

    String event = """
            {
                "version": "0",
                "id": "a5745004-fd1a-e96c-91a8-9f9cefcaa4ff",
                "detail-type": "charge.succeeded",
                "source": "aws.partner/stripe.com/ed_test_61Rc9ZIzCyVXIv5LR16Rc9WfUF9EoLgTRM8Gp72SOKAi",
                "account": "937808942071",
                "time": "2024-12-05T23:06:47Z",
                "region": "eu-west-1",
                "resources": [
                    "arn:aws:events:eu-west-1::event-source/aws.partner/stripe.com/ed_test_61Rc9ZIzCyVXIv5LR16Rc9WfUF9EoLgTRM8Gp72SOKAi"
                ],
                "detail": {
                    "id": "evt_3QSo8A2MCIkaoAFD0y2bNOj7",
                    "object": "event",
                    "api_version": "2024-09-30.acacia",
                    "created": 1733440006,
                    "data": {
                        "object": {
                            "id": "ch_3QSo8A2MCIkaoAFD0U2ksilm",
                            "object": "charge",
                            "amount": 413,
                            "amount_captured": 413,
                            "amount_refunded": 0,
                            "application": null,
                            "application_fee": null,
                            "application_fee_amount": null,
                            "balance_transaction": null,
                            "billing_details": {
                                "address": {
                                    "city": null,
                                    "country": null,
                                    "line1": null,
                                    "line2": null,
                                    "postal_code": null,
                                    "state": null
                                },
                                "email": null,
                                "name": null,
                                "phone": null
                            },
                            "calculated_statement_descriptor": "TEST-SANDBOX",
                            "captured": true,
                            "created": 1733440006,
                            "currency": "bgn",
                            "customer": "cus_RLScrIQ1GDjTpp",
                            "description": "Дарение в подкрепа на Стоян от Скритите таланти на България - Данислава Стимова",
                            "destination": null,
                            "dispute": null,
                            "disputed": false,
                            "failure_balance_transaction": null,
                            "failure_code": null,
                            "failure_message": null,
                            "fraud_details": {},
                            "invoice": null,
                            "livemode": false,
                            "metadata": {},
                            "on_behalf_of": null,
                            "order": null,
                            "outcome": {
                                "network_advice_code": null,
                                "network_decline_code": null,
                                "network_status": "approved_by_network",
                                "reason": null,
                                "risk_level": "normal",
                                "risk_score": 9,
                                "seller_message": "Payment complete.",
                                "type": "authorized"
                            },
                            "paid": true,
                            "payment_intent": "pi_3QSo8A2MCIkaoAFD0eiXamlz",
                            "payment_method": "pm_1QSlhm2MCIkaoAFDIwEIGTyC",
                            "payment_method_details": {
                                "card": {
                                    "amount_authorized": 413,
                                    "authorization_code": null,
                                    "brand": "visa",
                                    "checks": {
                                        "address_line1_check": null,
                                        "address_postal_code_check": null,
                                        "cvc_check": null
                                    },
                                    "country": "US",
                                    "exp_month": 3,
                                    "exp_year": 2030,
                                    "extended_authorization": {
                                        "status": "disabled"
                                    },
                                    "fingerprint": "vdPMhDzk8bfXCwo0",
                                    "funding": "credit",
                                    "incremental_authorization": {
                                        "status": "unavailable"
                                    },
                                    "installments": null,
                                    "last4": "4242",
                                    "mandate": null,
                                    "multicapture": {
                                        "status": "unavailable"
                                    },
                                    "network": "visa",
                                    "network_token": {
                                        "used": false
                                    },
                                    "overcapture": {
                                        "maximum_amount_capturable": 413,
                                        "status": "unavailable"
                                    },
                                    "three_d_secure": null,
                                    "wallet": null
                                },
                                "type": "card"
                            },
                            "radar_options": {},
                            "receipt_email": "velizar.kacharov@gmail.com",
                            "receipt_number": null,
                            "receipt_url": "https://pay.stripe.com/receipts/payment/CAcaFwoVYWNjdF8xUVNsWkoyTUNJa2FvQUZEKIfkyLoGMgZ0IWWBN986LBbEo7V3lKgg5fMa4GAiInHinjZZRBt7eKBwhM6k4g-2c4b6XJhEBe6Cy6KC",
                            "refunded": false,
                            "review": null,
                            "shipping": null,
                            "source": null,
                            "source_transfer": null,
                            "statement_descriptor": null,
                            "statement_descriptor_suffix": null,
                            "status": "succeeded",
                            "transfer_data": null,
                            "transfer_group": null
                        }
                    },
                    "livemode": false,
                    "pending_webhooks": 0,
                    "request": {
                        "id": "req_cVGxdSTyljRrs5",
                        "idempotency_key": "5a7579be-1fa6-4c72-96d1-20f0bab042ad"
                    },
                    "type": "charge.succeeded"
                }
            }
            """;

    @Test
    public void tsst() {
        ebf.apply(event);
    }
}
