package com.example.demo.controller;

import com.example.demo.model.Item;
import com.example.demo.service.ItemService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService service;

    public ItemController(ItemService service) {
        this.service = service;
    }

    @PostMapping
    public Mono<Item> createItem(@RequestBody Item item) {
        return service.createItem(item);
    }

    @GetMapping("/{id}")
    public Mono<Item> getItem(@PathVariable String id) {
        return service.getItem(id);
    }

    @GetMapping
    public Flux<Item> getAllItems() {
        return service.getAllItems();
    }
}
