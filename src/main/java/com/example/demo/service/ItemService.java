package com.example.demo.service;

import com.example.demo.model.Item;
import com.example.demo.repository.ItemRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@Service
public class ItemService {

    private final ItemRepository repository;

    public ItemService(ItemRepository repository) {
        this.repository = repository;
    }

    public Mono<Item> createItem(Item item) {
        return repository.save(item);
    }

    public Mono<Item> getItem(String id) {
        return repository.findById(id);
    }

    public Flux<Item> getAllItems() {
        return repository.findAll();
    }
}
