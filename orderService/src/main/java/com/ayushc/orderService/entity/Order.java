package com.ayushc.orderService.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {
    @Id
    private String id;
    private String itemName;
    private String status;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Order(String id, String itemName, String status) {
        this.id = id;
        this.itemName = itemName;
        this.status = status;
    }

    @PrePersist        //whenever repo.save(order) is called, this gets called too
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
