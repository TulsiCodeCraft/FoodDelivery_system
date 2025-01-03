package com.example.deliveryboy.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
public class DeliveryBoy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long empId; // Renamed to follow naming conventions

    @NotBlank(message = "Name cannot be empty")
    @Column(nullable = false)
    private String ename;

    @Email(message = "Email should be valid")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Vehicle cannot be empty")
    @Column(nullable = false)
    private String vehicle;

    @Pattern(regexp = "^\\d{10}$", message = "Phone number should be 10 digits") // Updated regex
    @Column(nullable = false, unique = true)
    private String phoneNumber;

    // Getters and setters
    public Long getEmpId() {
        return empId; // Updated getter
    }

    public void setEmpId(Long empId) {
        this.empId = empId; // Updated setter
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
