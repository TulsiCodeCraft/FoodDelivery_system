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

import com.example.deliveryboy.entity.Delivery;
import com.example.deliveryboy.repository.DeliveryRepository;
import com.example.deliveryboy.response.ResponseBean;

@RestController
@RequestMapping("/deliveries")
public class DeliveryController {
    private static final Logger logger = LoggerFactory.getLogger(DeliveryController.class);
    private static final String DELIVERY_NOT_FOUND_MSG = "Delivery not found for ID: %s";
    private static final String SUCCESS = "success"; // Define constant for "success"

    @Autowired
    private DeliveryRepository deliveryRepository;

    @GetMapping
    public ResponseBean<List<Delivery>> getAllDeliveries() {
        logger.info("Request received to get all deliveries");
        List<Delivery> deliveries = deliveryRepository.findAll();
        if (deliveries.isEmpty()) {
            logger.warn("No deliveries found");
            return new ResponseBean<>("warn", "No deliveries found", null);
        }
        logger.info("Returning all deliveries");
        return new ResponseBean<>(SUCCESS, "Deliveries retrieved successfully", deliveries);
    }

    @GetMapping("/{deliveryId}")
    public ResponseBean<Delivery> getDeliveryById(@PathVariable String deliveryId) {
        logger.info("Request received to get delivery by ID: {}", deliveryId);
        Optional<Delivery> delivery = deliveryRepository.findById(deliveryId);
        return delivery
                .map(d -> new ResponseBean<>(SUCCESS, "Delivery found", d))
                .orElseThrow(() -> new DeliveryNotFoundException(deliveryId));
    }

    @PostMapping
    public ResponseBean<String> createDelivery(@RequestBody Delivery delivery) {
        logger.info("Request received to create new delivery");
        Delivery savedDelivery = deliveryRepository.save(delivery);
        String message = "New delivery created successfully with ID: " + savedDelivery.getDeliveryId();
        logger.info(message);
        return new ResponseBean<>(SUCCESS, message, savedDelivery.toString());
    }

    @PutMapping("/{deliveryId}")
    public ResponseBean<String> updateDelivery(@PathVariable String deliveryId, @RequestBody Delivery updatedDelivery) {
        logger.info("Request received to update delivery with ID: {}", deliveryId);
        Optional<Delivery> existingDelivery = deliveryRepository.findById(deliveryId);
        if (existingDelivery.isPresent()) {
            updatedDelivery.setDeliveryId(deliveryId);
            Delivery savedDelivery = deliveryRepository.save(updatedDelivery);
            String message = "Delivery with ID: " + deliveryId + " updated successfully";
            logger.info(message);
            return new ResponseBean<>(SUCCESS, message, savedDelivery.toString());
        } else {
            throw new DeliveryNotFoundException(deliveryId);
        }
    }

    @DeleteMapping("/{deliveryId}")
    public ResponseBean<String> deleteDelivery(@PathVariable String deliveryId) {
        logger.info("Request received to delete delivery with ID: {}", deliveryId);
        Optional<Delivery> existingDelivery = deliveryRepository.findById(deliveryId);
        if (existingDelivery.isPresent()) {
            deliveryRepository.deleteById(deliveryId);
            String message = "Delivery with ID: " + deliveryId + " deleted successfully";
            logger.info(message);
            return new ResponseBean<>(SUCCESS, message, null);
        } else {
            throw new DeliveryNotFoundException(deliveryId);
        }
    }

    // Inner Exception Class for Delivery Not Found
    public static class DeliveryNotFoundException extends RuntimeException {
        public DeliveryNotFoundException(String deliveryId) {
            super(String.format(DELIVERY_NOT_FOUND_MSG, deliveryId));
        }
    }

    // Exception Handler for DeliveryNotFoundException
    @ExceptionHandler(DeliveryNotFoundException.class)
    public ResponseEntity<String> handleDeliveryNotFound(DeliveryNotFoundException ex) {
        logger.warn(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
