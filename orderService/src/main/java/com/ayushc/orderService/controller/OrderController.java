package com.ayushc.orderService.controller;

import com.ayushc.orderEvents.*;
import com.ayushc.orderService.dto.BaseResponse;
import com.ayushc.orderService.dto.OrderRequestDTO;
import com.ayushc.orderService.service.OrderEventProducer;
import com.ayushc.orderService.entity.Order;
import com.ayushc.orderService.exception.OrderNotFoundException;
import com.ayushc.orderService.repository.OrderRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderRepository repo;
    private final OrderEventProducer producer;

    public OrderController(OrderRepository repo, OrderEventProducer producer) {
        this.repo = repo;
        this.producer = producer;
    }


    // CREATE
    @PostMapping
    public ResponseEntity<BaseResponse<Order>> createOrder(@Valid @RequestBody OrderRequestDTO dto) {
        Order order = new Order(dto.id(), dto.itemName(), dto.status());

        Order saved = repo.save(order);

        //writing to postgres is done, now publish to kafka topic
        OrderCreatedEvent event = new OrderCreatedEvent(
                saved.getId(),
                saved.getItemName(),
                saved.getStatus(),
                Instant.now().toString()
        );
        producer.publishEvent(saved.getId(), event);  //publish to kafka topic, where key is the id

        return ResponseEntity  //return status to the client
                .status(HttpStatus.CREATED)
                .body(new BaseResponse<>(saved, "Order created successfully", 201, "Saved in DB"));
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<BaseResponse<List<Order>>> getOrders() {
        List<Order> orders = repo.findAll();
        return ResponseEntity.ok(
                new BaseResponse<>(orders, "Orders fetched successfully", 200, "Fetched all orders")
        );
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<Order>> getOrder(@PathVariable("id") String id) {
        Order order = repo.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        return ResponseEntity.ok(
                new BaseResponse<>(order, "Order found", 200, "Fetched order by ID")
        );
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<Order>> updateOrder(@PathVariable("id") String id,
                                                           @Valid @RequestBody OrderRequestDTO updatedDto) {
        Order existing = repo.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

        existing.setItemName(updatedDto.itemName());
        existing.setStatus(updatedDto.status());

        Order saved = repo.save(existing);

        OrderUpdatedEvent event = new OrderUpdatedEvent(
                saved.getId(),
                existing.getItemName(),
                saved.getItemName(),
                existing.getStatus(),
                saved.getStatus(),
                Instant.now().toString()
        );
        producer.publishEvent(saved.getId(), event);

        return ResponseEntity.ok(
                new BaseResponse<>(saved, "Order updated successfully", 200, "Updated in DB")
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteOrder(@PathVariable("id") String id) {
        Order toDelete = repo.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        //before actually deleting from postgres publish the delete event to the topic
        OrderDeletedEvent event = new OrderDeletedEvent(
                toDelete.getId(),
                toDelete.getItemName(),
                toDelete.getStatus(),
                Instant.now().toString()
        );
        producer.publishEvent(toDelete.getId(), event);
        repo.deleteById(id);
        return ResponseEntity.ok(
                new BaseResponse<>(null, "Order deleted successfully", 200, "Removed from DB")
        );
    }
}