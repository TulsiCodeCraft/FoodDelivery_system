package com.example.deliveryboy.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "\"orders\"") 
public class Order {

    @Id
    @Column(name = "\"order_id\"", length = 20)  
    private String orderId;

    @Column(name = "\"order_details\"", nullable = false, columnDefinition = "TEXT")
    private String orderDetails;

    @Column(name = "\"delivery_address\"", nullable = false, length = 255)
    private String deliveryAddress;

    @ManyToOne
    @JoinColumn(name = "\"user_id\"", referencedColumnName = "\"user_id\"")  
    private User user;

    // Constructors
    public Order() {}

    public Order(String orderId, String orderDetails, String deliveryAddress, User user) {
        this.orderId = orderId;
        this.orderDetails = orderDetails;
        this.deliveryAddress = deliveryAddress;
        this.user = user;
    }

    // Getters and setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(String orderDetails) {
        this.orderDetails = orderDetails;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

