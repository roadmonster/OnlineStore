package com.example.onlinestore.controller;

import com.example.onlinestore.model.Inventory;
import com.example.onlinestore.model.Product;
import com.example.onlinestore.model.User;
import com.example.onlinestore.model.UserProduct;
import com.example.onlinestore.repository.InventoryRepository;
import com.example.onlinestore.repository.ProductRepository;
import com.example.onlinestore.repository.UserProductRepository;
import com.example.onlinestore.repository.UserRepository;
import com.example.onlinestore.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/checkout")
public class CheckoutController {
    @Autowired
    private UserProductRepository userProductRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;


    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public boolean checkout(@RequestBody UserProduct userProduct) {
        // Verify that the user and product exist in the database
        User user = userProduct.getUser();
        Product product = userProduct.getProduct();

        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser == null) {
            return false;
        }

        Product existingProduct = productRepository.findById(product.getId()).orElse(null);
        if (existingProduct == null) {
            return false;
        }

        // Verify that there is enough inventory for the product
        Inventory existingInventory = inventoryRepository.findByProduct(existingProduct).orElse(null);
        if (existingInventory == null || existingInventory.getQuantity() < userProduct.getQuantity()) {
            return false;
        }

        // Calculate the total price of the user product
        double totalPrice = existingProduct.getPrice()* (userProduct.getQuantity());

        // Process the payment
        boolean paymentSuccessful = paymentService.processPayment(existingUser, totalPrice);
        if (!paymentSuccessful) {
            return false;
        }

        // Update the inventory
        existingInventory.setQuantity(existingInventory.getQuantity() - userProduct.getQuantity());
        inventoryRepository.save(existingInventory);

        // Add the user product to the user's cart
        userProduct.setPrice(existingProduct.getPrice());
        userProduct.setPurchaseDate(LocalDateTime.now());
        userProductRepository.save(userProduct);

        return true;
    }
}


