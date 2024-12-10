package com.saintolivetree.stripe_events_listener_service.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
@DynamoDbBean()
public class DonorNotification {

    public static enum NotificationStatus {
        DISABLED
    }

    private String donorId;
    private NotificationStatus status;

    public DonorNotification() {
    }

    public DonorNotification(String donorId, NotificationStatus status) {
        this.donorId = donorId;
        this.status = status;
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("DonorId")
    public String getDonorId() {
        return donorId;
    }

    public void setDonorId(String donorId) {
        this.donorId = donorId;
    }

    @DynamoDbAttribute("Status")
    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }
}
