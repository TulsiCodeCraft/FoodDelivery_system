package com.example.deliveryboy.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import java.math.BigDecimal;

@Entity
@Table(name = "\"bill\"")  // Quoted table name
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"bill_id\"")  // Primary key with auto-increment
    private Long billId;

    @ManyToOne
    @JoinColumn(name = "\"order_id\"", referencedColumnName = "\"order_id\"")  // Foreign key referencing Order
    private Order order;

    @Column(name = "\"amount\"", nullable = false, precision = 10, scale = 2)  // Amount with 2 decimal places
    private BigDecimal amount;  // Changed from double to BigDecimal

    @Column(name = "\"transaction_id\"", nullable = false, unique = true, length = 50)  // Unique transaction ID
    private String transactionId;

    // Constructors
    public Bill() {}

    public Bill(Order order, BigDecimal amount, String transactionId) {  // BigDecimal for amount
        this.order = order;
        this.amount = amount;
        this.transactionId = transactionId;
    }

    // Getters and setters
    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
