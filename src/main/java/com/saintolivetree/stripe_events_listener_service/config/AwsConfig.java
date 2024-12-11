package com.saintolivetree.stripe_events_listener_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
public class AwsConfig {

    @Value("${amazon.dynamodb.url:}")
    private String dynamodbUrl;

    @Value("${amazon.aws.region}")
    private String awsRegion;

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient client) {
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(client)
                .build();

        return enhancedClient;
    }

    @Bean
    @Profile("dev")
    public DynamoDbClient dynamoDbDevClient() {
        DynamoDbClient client = DynamoDbClient.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .endpointOverride(URI.create(dynamodbUrl))
                .build();
        return client;
    }

    @Bean
    @Profile("prod")
    public DynamoDbClient dynamoDbProdClient() {
        DynamoDbClient client = DynamoDbClient.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(InstanceProfileCredentialsProvider.create())
                .build();
        return client;
    }
}