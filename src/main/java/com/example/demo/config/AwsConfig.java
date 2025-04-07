package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import java.net.URI;

@Configuration
public class AwsConfig {

  @Bean
  public DynamoDbAsyncClient dynamoDbAsyncClient() {
    return DynamoDbAsyncClient.builder()
            .region(Region.US_EAST_1)
            // Uncomment the following line to use DynamoDB Local for testing
            // .endpointOverride(URI.create("http://localhost:8000"))
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build();
  }
}
