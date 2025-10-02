package com.ayushc.orderService.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void simulatePost() throws Exception {
        String reqBody1 = """
                {
                    "id": "123",
                    "itemName": "drone",
                    "status": "shipped"
                }
                """;

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/orders")
                                .contentType("application/json")
                                .content(reqBody1))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Order created successfully"))
                .andExpect(jsonPath("$.data.itemName").value("drone"))
                .andExpect(jsonPath("$.data.status").value("shipped"));
    }

    @Test
    public void simulateGetAllSuccess() throws Exception {
        String reqBody = """
                {
                    "id": "200",
                    "itemName": "keyboard",
                    "status": "placed"
                }
                """;

        this.mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                .contentType("application/json")
                .content(reqBody));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Orders fetched successfully"));
    }

    @Test
    public void simulateGetOneSuccess() throws Exception {
        String reqBody = """
                {
                    "id": "300",
                    "itemName": "mouse",
                    "status": "shipped"
                }
                """;

        this.mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                .contentType("application/json")
                .content(reqBody));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/orders/300"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Order found"))
                .andExpect(jsonPath("$.data.itemName").value("mouse"));
    }

    @Test
    public void simulateGetOneFailure() throws Exception {
        String id = "145";

        this.mockMvc.perform(MockMvcRequestBuilders.get("/orders/" + id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Order with ID " + id + " not found"))
                .andExpect(jsonPath("$.details").value("Order not found in DB"));
    }

    @Test
    public void simulatePutOneSuccess() throws Exception {
        String createBody = """
                {
                    "id": "400",
                    "itemName": "book",
                    "status": "placed"
                }
                """;

        this.mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                .contentType("application/json")
                .content(createBody));

        String newBody = """
                {
                    "id": "400",
                    "itemName": "pens",
                    "status": "delivered"
                }
                """;

        this.mockMvc.perform(MockMvcRequestBuilders.put("/orders/400")
                        .contentType("application/json")
                        .content(newBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Order updated successfully"))
                .andExpect(jsonPath("$.data.itemName").value("pens"))
                .andExpect(jsonPath("$.data.status").value("delivered"));
    }

    @Test
    public void simulatePutOneFailure() throws Exception {
        String newBody = """
                {
                    "id": "500",
                    "itemName": "banana",
                    "status": "delivered"
                }
                """;

        this.mockMvc.perform(MockMvcRequestBuilders.put("/orders/500")
                        .contentType("application/json")
                        .content(newBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Order with ID 500 not found"))
                .andExpect(jsonPath("$.details").value("Order not found in DB"));
    }

    @Test
    public void simulateDeleteOneSuccess() throws Exception {
        String reqBody = """
                {
                    "id": "600",
                    "itemName": "tablet",
                    "status": "placed"
                }
                """;

        this.mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                .contentType("application/json")
                .content(reqBody));

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/orders/600"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Order deleted successfully"));
    }

    @Test
    public void simulateDeleteOneFailure() throws Exception {
        String id = "700";

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/orders/" + id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Order with ID " + id + " not found"))
                .andExpect(jsonPath("$.details").value("Order not found in DB"));
    }
}