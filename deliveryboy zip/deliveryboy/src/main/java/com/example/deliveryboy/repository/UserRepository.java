package com.example.deliveryboy.repository;

import com.example.deliveryboy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
