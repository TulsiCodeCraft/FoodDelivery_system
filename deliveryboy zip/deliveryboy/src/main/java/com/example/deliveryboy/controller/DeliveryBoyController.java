package com.example.deliveryboy.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.deliveryboy.entity.DeliveryBoy;
import com.example.deliveryboy.repository.DeliveryBoyRepository;
import com.example.deliveryboy.response.ResponseBean;

@RestController
@RequestMapping("/delivery-boys")
@Validated
public class DeliveryBoyController {
    private static final Logger logger = LoggerFactory.getLogger(DeliveryBoyController.class);
    private static final String SUCCESS = "success";
    private static final String NOT_FOUND = "Delivery boy with ID: {} not found";

    @Autowired
    private DeliveryBoyRepository deliveryBoyRepository;

    @GetMapping
    public ResponseBean<List<DeliveryBoy>> getAllDeliveryBoys() {
        logger.info("Request received to get all delivery boys");
        List<DeliveryBoy> deliveryBoys = deliveryBoyRepository.findAll();
        if (deliveryBoys.isEmpty()) {
            logger.warn("No delivery boys found");
            return new ResponseBean<>("warn", "No delivery boys found", null);
        }
        logger.info("Returning list of {} delivery boys", deliveryBoys.size());
        return new ResponseBean<>(SUCCESS, "Delivery boys retrieved successfully", deliveryBoys);
    }

    @GetMapping("/{empId}")
    public ResponseBean<DeliveryBoy> getDeliveryBoyById(@PathVariable Long empId) {
        logger.info("Request received to get delivery boy by ID: {}", empId);
        Optional<DeliveryBoy> deliveryBoy = deliveryBoyRepository.findById(empId);
        return deliveryBoy
                .map(db -> new ResponseBean<>(SUCCESS, "Delivery boy retrieved successfully", db))
                .orElseThrow(() -> {
                    logger.error(NOT_FOUND, empId);
                    return new DeliveryBoyNotFoundException(empId);
                });
    }

    @PostMapping
    public ResponseBean<String> createDeliveryBoy(@RequestBody DeliveryBoy deliveryBoy) {
        logger.info("Request received to create new delivery boy");
        deliveryBoyRepository.save(deliveryBoy);
        String message = "New delivery boy created with ID: " + deliveryBoy.getEmpId(); // Updated to match naming
        logger.info(message);
        return new ResponseBean<>(SUCCESS, message, null);
    }

    @PutMapping("/{empId}")
    public ResponseBean<String> updateDeliveryBoy(@PathVariable Long empId,
            @RequestBody DeliveryBoy updatedDeliveryBoy) {
        logger.info("Request received to update delivery boy with ID: {}", empId);
        Optional<DeliveryBoy> existingDeliveryBoy = deliveryBoyRepository.findById(empId);
        if (existingDeliveryBoy.isPresent()) {
            updatedDeliveryBoy.setEmpId(empId); // Updated to match naming
            deliveryBoyRepository.save(updatedDeliveryBoy);
            String message = "Delivery boy with ID: " + empId + " updated successfully";
            logger.info(message);
            return new ResponseBean<>(SUCCESS, message, null);
        } else {
            logger.error(NOT_FOUND, empId);
            throw new DeliveryBoyNotFoundException(empId);
        }
    }

    @DeleteMapping("/{empId}")
    public ResponseBean<String> deleteDeliveryBoy(@PathVariable Long empId) {
        logger.info("Request received to delete delivery boy with ID: {}", empId);
        Optional<DeliveryBoy> existingDeliveryBoy = deliveryBoyRepository.findById(empId);
        if (existingDeliveryBoy.isPresent()) {
            deliveryBoyRepository.deleteById(empId);
            String message = "Delivery boy with ID: " + empId + " deleted successfully";
            logger.info(message);
            return new ResponseBean<>(SUCCESS, message, null);
        } else {
            logger.error(NOT_FOUND, empId);
            throw new DeliveryBoyNotFoundException(empId);
        }
    }

    public static class DeliveryBoyNotFoundException extends RuntimeException {
        public DeliveryBoyNotFoundException(Long empId) {
            super(NOT_FOUND.replace("{}", String.valueOf(empId)));
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseBean<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage) // Updated to use FieldError
                .collect(Collectors.joining(", "));
        logger.warn("Validation error: {}", errorMessage);
        return new ResponseBean<>("error", errorMessage, null);
    }

}
