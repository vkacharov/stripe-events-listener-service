package com.saintolivetree.stripe_events_listener_service.repository;

import com.saintolivetree.stripe_events_listener_service.model.DonorNotification;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
public class DonorNotificationRepository {
    private final DynamoDbTable<DonorNotification> table;

    public DonorNotificationRepository(DynamoDbEnhancedClient enhancedClient) {
        this.table = enhancedClient.table("DonorNotification", TableSchema.fromBean(DonorNotification.class));
    }

    public void save(DonorNotification entity) {
        table.putItem(entity);
    }

    public DonorNotification findById(String id) {
        return table.getItem(Key.builder().partitionValue(id).build());
    }
}
