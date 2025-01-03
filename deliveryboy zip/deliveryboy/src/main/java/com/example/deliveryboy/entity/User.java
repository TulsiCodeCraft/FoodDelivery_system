package com.example.deliveryboy.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "\"users\"")  // Quoted table name if it's lowercase or reserved
public class User {

    @Id
    @Column(name = "\"user_id\"", length = 3)  // Quoted column name if necessary
    @NotBlank(message = "User ID cannot be blank")
    private String userId;

    @Column(name = "\"name\"", nullable = false, length = 100)
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Column(name = "\"email\"", nullable = false, unique = true, length = 100)
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String email;

    @Column(name = "\"phone_num\"", nullable = false, length = 15)
    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be 10 digits")
    private String phoneNum;

    // Constructors
    public User() {}

    public User(String userId, String name, String email, String phoneNum) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phoneNum = phoneNum;
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
