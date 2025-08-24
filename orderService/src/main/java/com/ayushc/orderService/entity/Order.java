package com.ayushc.orderService.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String itemName;
    private String status;
}
