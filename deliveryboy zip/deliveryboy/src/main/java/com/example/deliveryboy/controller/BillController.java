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

import com.example.deliveryboy.entity.Bill;
import com.example.deliveryboy.repository.BillRepository;
import com.example.deliveryboy.response.ResponseBean;

@RestController
@RequestMapping("/bills")
public class BillController {
    private static final Logger logger = LoggerFactory.getLogger(BillController.class);
    private static final String SUCCESS = "success";
    private static final String BILL_NOT_FOUND_MSG = "Bill not found with ID: %d";

    @Autowired
    private BillRepository billRepository;

    @GetMapping
    public ResponseBean<List<Bill>> getAllBills() {
        logger.info("Request received to get all bills");
        List<Bill> bills = billRepository.findAll();
        if (bills.isEmpty()) {
            logger.warn("No bills found");
            return new ResponseBean<>("warn", "No bills found", null);
        }
        return new ResponseBean<>(SUCCESS, "Bills retrieved successfully", bills);
    }

    @GetMapping("/{billId}")
    public ResponseBean<Bill> getBillById(@PathVariable Long billId) {
        logger.info("Request received to get bill by ID: {}", billId);
        Optional<Bill> bill = billRepository.findById(billId);
        if (bill.isPresent()) {
            return new ResponseBean<>(SUCCESS, "Bill retrieved successfully", bill.get());
        } else {
            logger.error(String.format(BILL_NOT_FOUND_MSG, billId));
            throw new BillNotFoundException(billId);
        }
    }

    @PostMapping("/create")
    public ResponseBean<String> createBill(@RequestBody Bill bill) {
        logger.info("Request received to create a new bill");
        billRepository.save(bill);
        return new ResponseBean<>(SUCCESS, "Bill created successfully", null);
    }

    @PutMapping("/update/{billId}")
    public ResponseBean<String> updateBill(@PathVariable Long billId, @RequestBody Bill updatedBill) {
        logger.info("Request received to update bill with ID: {}", billId);
        Optional<Bill> existingBill = billRepository.findById(billId);
        if (existingBill.isPresent()) {
            updatedBill.setBillId(billId);
            billRepository.save(updatedBill);
            logger.info("Bill with ID: {} updated successfully", billId);
            return new ResponseBean<>(SUCCESS, "Bill updated successfully", null);
        } else {
            logger.error(String.format(BILL_NOT_FOUND_MSG, billId));
            throw new BillNotFoundException(billId);
        }
    }

    @DeleteMapping("/delete/{billId}")
    public ResponseBean<String> deleteBill(@PathVariable Long billId) {
        logger.info("Request received to delete bill with ID: {}", billId);
        Optional<Bill> existingBill = billRepository.findById(billId);
        if (existingBill.isPresent()) {
            billRepository.delete(existingBill.get());
            logger.info("Bill with ID: {} deleted successfully", billId);
            return new ResponseBean<>(SUCCESS, "Bill deleted successfully", null);
        } else {
            logger.error(String.format(BILL_NOT_FOUND_MSG, billId));
            throw new BillNotFoundException(billId);
        }
    }

    // Inner Exception Class for Bill Not Found
    public static class BillNotFoundException extends RuntimeException {
        public BillNotFoundException(Long billId) {
            super(String.format(BILL_NOT_FOUND_MSG, billId));
        }
    }

    // Exception Handler for BillNotFoundException
    @ExceptionHandler(BillNotFoundException.class)
    public ResponseEntity<String> handleBillNotFound(BillNotFoundException ex) {
        logger.warn(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
