package com.example.onlinestore.service;

import com.example.onlinestore.model.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentService {
    public boolean processPayment(User user, double amount) {
        // Call the payment gateway API to process the payment
        // Return true if the payment was successful, false otherwise
        return true;
    }
}

