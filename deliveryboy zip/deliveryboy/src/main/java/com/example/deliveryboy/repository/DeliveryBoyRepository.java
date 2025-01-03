package com.example.deliveryboy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.deliveryboy.entity.DeliveryBoy;

public interface DeliveryBoyRepository extends JpaRepository<DeliveryBoy, Long> {
}
