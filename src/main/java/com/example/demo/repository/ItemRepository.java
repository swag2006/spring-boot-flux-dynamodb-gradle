package com.example.demo.repository;

import com.example.demo.model.Item;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Repository
public class ItemRepository {

    private final DynamoDbAsyncClient dynamoDbAsyncClient;
    private final String tableName;

    public ItemRepository(DynamoDbAsyncClient dynamoDbAsyncClient, 
                          @Value("${aws.dynamodb.tableName:Items}") String tableName) {
        this.dynamoDbAsyncClient = dynamoDbAsyncClient;
        this.tableName = tableName;
    }

    public Mono<Item> save(Item item) {
        Map<String, AttributeValue> itemValues = new HashMap<>();
        itemValues.put("id", AttributeValue.builder().s(item.getId()).build());
        itemValues.put("name", AttributeValue.builder().s(item.getName()).build());
        itemValues.put("description", AttributeValue.builder().s(item.getDescription()).build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(itemValues)
                .build();

        CompletableFuture<?> future = dynamoDbAsyncClient.putItem(request);
        return Mono.fromFuture(future)
                   .thenReturn(item);
    }

    public Mono<Item> findById(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", AttributeValue.builder().s(id).build());

        GetItemRequest request = GetItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .build();

        return Mono.fromFuture(dynamoDbAsyncClient.getItem(request))
                   .flatMap(response -> {
                       if (response.hasItem() && response.item() != null && !response.item().isEmpty()) {
                           Map<String, AttributeValue> itemMap = response.item();
                           Item item = new Item();
                           item.setId(itemMap.get("id").s());
                           item.setName(itemMap.get("name").s());
                           item.setDescription(itemMap.get("description").s());
                           return Mono.just(item);
                       } else {
                           return Mono.empty();
                       }
                   });
    }

    public Flux<Item> findAll() {
        ScanRequest request = ScanRequest.builder()
                .tableName(tableName)
                .build();

        return Mono.fromFuture(dynamoDbAsyncClient.scan(request))
                .flatMapMany(response -> Flux.fromIterable(response.items()))
                .map(itemMap -> {
                    Item item = new Item();
                    item.setId(itemMap.get("id").s());
                    item.setName(itemMap.get("name").s());
                    item.setDescription(itemMap.get("description").s());
                    return item;
                });
    }
}
