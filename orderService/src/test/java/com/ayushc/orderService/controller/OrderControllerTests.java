package com.ayushc.orderService.controller;

import com.ayushc.orderService.dto.BaseResponse;
import com.ayushc.orderService.dto.OrderRequestDTO;
import com.ayushc.orderService.entity.Order;
import com.ayushc.orderService.repository.OrderRepository;
import com.ayushc.orderService.service.OrderEventProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


public class OrderControllerTests {
    //since the controller class is not static, as it depends on the OrderRepository interface
    //hence, we make a mock order repository and then inject that mock into the controller

    @Mock
    private OrderEventProducer producer;

    @Mock
    private OrderRepository repo;

    @InjectMocks
    private OrderController controller;      //inject the above mock here


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);    //to init Mock and InjectMock
    }

    @Test
    public void createOrderTest() {
        OrderRequestDTO orderDto = new OrderRequestDTO("1", "ray-ban-glasses", "delivered");
        Order order = new Order(orderDto.id(), orderDto.itemName(), orderDto.status());

        when(repo.save(order)).thenReturn(order);     //this indicates to Mockito that when
        //controller calls repo.save(order) instead of writing to the actual database, just return
        //the order right back to the createOrder() method

        ResponseEntity<BaseResponse<Order>> response = controller.createOrder(orderDto);

        assertNotNull(response);
        assertEquals(201, Objects.requireNonNull(response.getBody()).status());
        assertEquals("ray-ban-glasses", response.getBody().data().getItemName());

        verify(repo, times(1)).save(order);   //verify that repo.save(order)
        //was called at least once
    }

    @Test
    public void getOrderTest() {
        Order order = new Order("2", "drone", "placed");

        when(repo.findById(order.getId())).thenReturn(Optional.of(order));

        ResponseEntity<BaseResponse<Order>> response = controller.getOrder("2");

        assertEquals("drone", Objects.requireNonNull(response.getBody()).data().getItemName());
        verify(repo).findById("2");
    }

    @Test
    public void getOrdersTest() {
        // Arrange
        Order o1 = new Order("2", "drone", "placed");
        Order o2 = new Order("1", "ray-ban-glasses", "shipped");
        List<Order> orders = List.of(o1, o2);

        when(repo.findAll()).thenReturn(orders);

        ResponseEntity<BaseResponse<List<Order>>> response = controller.getOrders();

        assertNotNull(response);
        assertEquals(200, Objects.requireNonNull(response.getBody()).status());
        assertEquals(2, response.getBody().data().size());
        assertEquals("drone", response.getBody().data().get(0).getItemName());

        verify(repo, times(1)).findAll();
    }

    @Test
    public void getOrderNotFoundTest() {
        when(repo.findById("999")).thenReturn(Optional.empty());

        try {
            controller.getOrder("999");
        } catch (Exception ex) {
            assertEquals("Order with ID 999 not found", ex.getMessage());
        }

        verify(repo).findById("999");
    }

    @Test
    public void updateOrderTest() {
        // Arrange
        Order existing = new Order("1", "drone", "placed");
        OrderRequestDTO updateDto = new OrderRequestDTO("1", "drone", "shipped");
        Order update = new Order(updateDto.id(), updateDto.itemName(), updateDto.status());

        when(repo.findById("1")).thenReturn(Optional.of(existing));
        when(repo.save(existing)).thenReturn(update);

        ResponseEntity<BaseResponse<Order>> response = controller.updateOrder("1", updateDto);

        assertNotNull(response);
        assertEquals(200, Objects.requireNonNull(response.getBody()).status());
        assertEquals("shipped", response.getBody().data().getStatus());

        verify(repo).findById("1");
        verify(repo).save(existing);
    }

    @Test
    public void updateOrderNotFoundTest() {
        OrderRequestDTO updateDto = new OrderRequestDTO("10", "shoes", "delivered");

        when(repo.findById("10")).thenReturn(Optional.empty());

        try {
            controller.updateOrder("10", updateDto);
        } catch (Exception ex) {
            assertEquals("Order with ID 10 not found", ex.getMessage());
        }

        verify(repo).findById("10");
    }

    @Test
    public void deleteOrderTest() {
        Order order = new Order("1", "drone", "placed");

        when(repo.findById("1")).thenReturn(Optional.of(order));
        doNothing().when(repo).deleteById("1");

        ResponseEntity<BaseResponse<Void>> response = controller.deleteOrder("1");

        assertNotNull(response);
        assertEquals(200, Objects.requireNonNull(response.getBody()).status());
        assertEquals("Order deleted successfully", response.getBody().message());

        verify(repo).findById("1");
        verify(repo).deleteById("1");
    }

    @Test
    public void deleteOrderNotFoundTest() {
        when(repo.findById("5")).thenReturn(Optional.empty());

        try {
            controller.deleteOrder("5");
        } catch (Exception ex) {
            assertEquals("Order with ID 5 not found", ex.getMessage());
        }

        verify(repo).findById("5");
        verify(repo, never()).deleteById("5");
    }

}
