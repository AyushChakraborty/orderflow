package com.ayushc.orderService.repository;

import com.ayushc.orderService.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

//Order is the type of entity, String is the type of the primary key
public interface OrderRepository extends JpaRepository<Order, String>{
}
