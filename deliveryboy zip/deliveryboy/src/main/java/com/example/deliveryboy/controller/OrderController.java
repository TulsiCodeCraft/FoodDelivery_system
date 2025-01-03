package com.example.deliveryboy.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.deliveryboy.entity.Order;
import com.example.deliveryboy.repository.OrderRepository;
import com.example.deliveryboy.response.ResponseBean;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private static final String ORDER_NOT_FOUND_MSG = "Order not found for ID: %s";
    private static final String SUCCESS = "success"; // Define constant for "success"

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping
    public ResponseBean<List<Order>> getAllOrders() {
        logger.info("Request received to get all orders");
        List<Order> orders = orderRepository.findAll();
        if (orders.isEmpty()) {
            logger.warn("No orders found");
            return new ResponseBean<>("warn", "No orders found", null);
        }
        logger.info("Returning all orders to the client.");
        return new ResponseBean<>(SUCCESS, "Orders retrieved successfully", orders);
    }

    @GetMapping("/{orderId}")
    public ResponseBean<Order> getOrderById(@PathVariable String orderId) {
        logger.info("Request received to get order by ID: {}", orderId);
        Optional<Order> order = orderRepository.findById(orderId);
        return order
                .map(o -> new ResponseBean<>(SUCCESS, "Order found", o))
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    @PostMapping
    public ResponseBean<String> createOrder(@RequestBody Order order) {
        logger.info("Request received to create new order");
        Order savedOrder = orderRepository.save(order);
        String message = "New order created successfully with ID: " + savedOrder.getOrderId();
        logger.info(message);
        return new ResponseBean<>(SUCCESS, message, savedOrder.toString());
    }

    @PutMapping("/{orderId}")
    public ResponseBean<String> updateOrder(@PathVariable String orderId, @RequestBody Order updatedOrder) {
        logger.info("Request received to update order with ID: {}", orderId);
        Optional<Order> existingOrder = orderRepository.findById(orderId);
        if (existingOrder.isPresent()) {
            updatedOrder.setOrderId(orderId);
            Order savedOrder = orderRepository.save(updatedOrder);
            String message = "Order with ID: " + orderId + " updated successfully";
            logger.info(message);
            return new ResponseBean<>(SUCCESS, message, savedOrder.toString());
        } else {
            throw new OrderNotFoundException(orderId);
        }
    }

    @DeleteMapping("/{orderId}")
    public ResponseBean<String> deleteOrder(@PathVariable String orderId) {
        logger.info("Request received to delete order with ID: {}", orderId);
        Optional<Order> existingOrder = orderRepository.findById(orderId);
        if (existingOrder.isPresent()) {
            orderRepository.deleteById(orderId);
            String message = "Order with ID: " + orderId + " deleted successfully";
            logger.info(message);
            return new ResponseBean<>(SUCCESS, message, null);
        } else {
            throw new OrderNotFoundException(orderId);
        }
    }

    // Inner Exception Class for Order Not Found
    public static class OrderNotFoundException extends RuntimeException {
        public OrderNotFoundException(String orderId) {
            super(String.format(ORDER_NOT_FOUND_MSG, orderId));
        }
    }

    // Exception Handler for OrderNotFoundException
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<String> handleOrderNotFound(OrderNotFoundException ex) {
        logger.warn(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
