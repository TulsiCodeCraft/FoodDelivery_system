package com.example.deliveryboy.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "\"delivery\"")  // Quoted table name
public class Delivery {

    @Id
    @Column(name = "\"delivery_id\"", length = 20)  // Primary key
    private String deliveryId;

    @ManyToOne
    @JoinColumn(name = "\"user_id\"", referencedColumnName = "\"user_id\"")  // Foreign key referencing User
    private User user;

    @ManyToOne
    @JoinColumn(name = "\"order_id\"", referencedColumnName = "\"order_id\"")  // Foreign key referencing Order
    private Order order;

    @ManyToOne
    @JoinColumn(name = "\"emp_id\"", referencedColumnName = "\"emp_id\"")  // Foreign key referencing DeliveryBoy
    private DeliveryBoy deliveryBoy;

    @Column(name = "\"delivery_status\"", nullable = false)
    private boolean deliveryStatus;

    // Constructors
    public Delivery() {}

    public Delivery(String deliveryId, User user, Order order, DeliveryBoy deliveryBoy, boolean deliveryStatus) {
        this.deliveryId = deliveryId;
        this.user = user;
        this.order = order;
        this.deliveryBoy = deliveryBoy;
        this.deliveryStatus = deliveryStatus;
    }

    // Getters and setters
    public String getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public DeliveryBoy getDeliveryBoy() {
        return deliveryBoy;
    }

    public void setDeliveryBoy(DeliveryBoy deliveryBoy) {
        this.deliveryBoy = deliveryBoy;
    }

    public boolean isDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(boolean deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
}
