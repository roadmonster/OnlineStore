package com.example.onlinestore.controller;

import com.example.onlinestore.model.*;
import com.example.onlinestore.repository.*;
import com.example.onlinestore.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

@RestController
@RequestMapping("/shopping-carts")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

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

    @GetMapping("/{userId}")
    public ShoppingCart getShoppingCartByUserId(@PathVariable Long userId) {
        User user = new User();
        user.setId(userId);
        return shoppingCartRepository.findByUser(user).orElse(null);
    }

    @PostMapping("/{userId}/add-product")
    public ShoppingCart addProductToShoppingCart(@PathVariable Long userId, @RequestBody UserProduct userProduct) {
        User user = new User();
        user.setId(userId);

        // Verify that the user and product exist in the database
        User existingUser = userRepository.findById(userId).orElse(null);
        if (existingUser == null) {
            return null;
        }

        Product existingProduct = productRepository.findById(userProduct.getProduct().getId()).orElse(null);
        if (existingProduct == null) {
            return null;
        }

        // Verify that there is enough inventory for the product
        Inventory existingInventory = inventoryRepository.findByProduct(existingProduct).orElse(null);
        if (existingInventory == null || existingInventory.getQuantity() < userProduct.getQuantity()) {
            return null;
        }

        // Update the inventory
        existingInventory.setQuantity(existingInventory.getQuantity() - userProduct.getQuantity());
        inventoryRepository.save(existingInventory);

        // Add the user product to the user's cart
        userProduct.setUser(existingUser);
        userProduct.setPrice(existingProduct.getPrice());
        userProduct.setPurchaseDate(null);

        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(existingUser).orElse(null);
        if (shoppingCart == null) {
            shoppingCart = new ShoppingCart();
            shoppingCart.setUser(existingUser);
            shoppingCart.setUserProducts(new ArrayList<>());
        }
        shoppingCart.getUserProducts().add(userProduct);
        userProduct.setShoppingCart(shoppingCart);

        return shoppingCartRepository.save(shoppingCart);
    }

    @PostMapping("/{userId}/checkout")
    public boolean checkoutShoppingCart(@PathVariable Long userId) {
        User user = new User();
        user.setId(userId);

        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user).orElse(null);
        if (shoppingCart == null) {
            return false;
        }

        boolean checkoutSuccessful = true;

        for (UserProduct userProduct : shoppingCart.getUserProducts()) {
            // Check if the product still exists and if there is enough inventory for it
            Product existingProduct = productRepository.findById(userProduct.getProduct().getId()).orElse(null);
            if (existingProduct == null) {
                checkoutSuccessful = false;
                continue;
            }

            Inventory existingInventory = inventoryRepository.findByProduct(existingProduct).orElse(null);
            if (existingInventory == null || existingInventory.getQuantity() < userProduct.getQuantity()) {
                checkoutSuccessful = false;
                continue;
            }

            // Calculate the total price of the user product
            double totalPrice = existingProduct.getPrice() * (userProduct.getQuantity());

            // Process the payment

            boolean paymentSuccessful = paymentService.processPayment(shoppingCart.getUser(), totalPrice);
            if (!paymentSuccessful) {
                checkoutSuccessful = false;
                continue;
            }

            // Update the inventory
            existingInventory.setQuantity(existingInventory.getQuantity() - userProduct.getQuantity());
            inventoryRepository.save(existingInventory);

            // Update the purchase date of the user product
            userProduct.setPurchaseDate(LocalDateTime.now());
            userProductRepository.save(userProduct);
        }

        if (checkoutSuccessful) {
            // Clear the shopping cart
            shoppingCart.getUserProducts().clear();
            shoppingCartRepository.save(shoppingCart);
        }

        return checkoutSuccessful;
    }
}


