package com.ayushc.orderService.controller;

import com.ayushc.orderService.dto.BaseResponse;
import com.ayushc.orderService.entity.Order;
import com.ayushc.orderService.exception.OrderNotFoundException;
import com.ayushc.orderService.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderRepository repo;

    public OrderController(OrderRepository repo) {
        this.repo = repo;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<BaseResponse<Order>> createOrder(@RequestBody Order order) {
        Order saved = repo.save(order);
        return ResponseEntity
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
    public ResponseEntity<BaseResponse<Order>> getOrder(@PathVariable String id) {
        Order order = repo.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        return ResponseEntity.ok(
                new BaseResponse<>(order, "Order found", 200, "Fetched order by ID")
        );
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<Order>> updateOrder(@PathVariable String id,
                                                           @RequestBody Order updatedOrder) {
        Order existing = repo.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

        existing.setItemName(updatedOrder.getItemName());
        existing.setStatus(updatedOrder.getStatus());

        Order saved = repo.save(existing);

        return ResponseEntity.ok(
                new BaseResponse<>(saved, "Order updated successfully", 200, "Updated in DB")
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteOrder(@PathVariable String id) {
        if (!repo.existsById(id)) {
            throw new OrderNotFoundException(id);
        }
        repo.deleteById(id);
        return ResponseEntity.ok(
                new BaseResponse<>(null, "Order deleted successfully", 200, "Removed from DB")
        );
    }
}