package com.example.deliveryboy.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger; // Import for Collectors
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

import com.example.deliveryboy.entity.User;
import com.example.deliveryboy.repository.UserRepository;
import com.example.deliveryboy.response.ResponseBean;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private static final String USER_NOT_FOUND_MSG = "User not found for ID: %s";
    private static final String SUCCESS = "success"; // Define a constant

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseBean<List<User>> getAllUsers() {
        logger.info("Request received to get all users");
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            logger.warn("No users found");
            return new ResponseBean<>("warn", "No users found", null);
        }
        logger.info("Returning all users to the client.");
        return new ResponseBean<>(SUCCESS, "Users retrieved successfully", users);
    }

    @GetMapping("/{id}")
    public ResponseBean<User> getUserById(@PathVariable("id") String userId) {
        logger.info("Request received to get user by ID: {}", userId);
        Optional<User> user = userRepository.findById(userId);
        return user
                .map(u -> new ResponseBean<>(SUCCESS, "User found", u))
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @PostMapping
    public ResponseBean<String> createUser(@RequestBody User user) {
        logger.info("Request received to create a new user: {}", user);
        User savedUser = userRepository.save(user);
        String message = "New user created with ID: " + savedUser.getUserId();
        logger.info(message);
        return new ResponseBean<>(SUCCESS, message, savedUser.toString());
    }

    @PutMapping("/{id}")
    public ResponseBean<String> updateUser(@PathVariable("id") String userId, @RequestBody User userDetails) {
        logger.info("Request received to update user with ID: {}", userId);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setName(userDetails.getName());
            user.setEmail(userDetails.getEmail());
            user.setPhoneNum(userDetails.getPhoneNum());
            userRepository.save(user);
            String message = "User with ID: " + userId + " updated successfully";
            logger.info(message);
            return new ResponseBean<>(SUCCESS, message, user.toString());
        } else {
            throw new UserNotFoundException(userId);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseBean<String> deleteUser(@PathVariable("id") String userId) {
        logger.info("Request received to delete user with ID: {}", userId);
        Optional<User> existingUser = userRepository.findById(userId);
        if (existingUser.isPresent()) {
            userRepository.deleteById(userId);
            String successMessage = "User with ID: " + userId + " deleted successfully";
            logger.info(successMessage);
            return new ResponseBean<>(SUCCESS, successMessage, null);
        } else {
            throw new UserNotFoundException(userId);
        }
    }

    // Inner Exception Class for User Not Found
    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String userId) {
            super(String.format(USER_NOT_FOUND_MSG, userId));
        }
    }

    // Exception Handler for UserNotFoundException
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
        logger.warn(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
