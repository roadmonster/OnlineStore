package com.example.onlinestore.controller;

import com.example.onlinestore.model.UserProduct;
import com.example.onlinestore.repository.UserProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-products")
public class UserProductController {
    @Autowired
    private UserProductRepository userProductRepository;

    @PostMapping
    public UserProduct createUserProduct(@RequestBody UserProduct userProduct) {
        return userProductRepository.save(userProduct);
    }

    @GetMapping("/{id}")
    public UserProduct getUserProductById(@PathVariable Long id) {
        return userProductRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public UserProduct updateUserProduct(@PathVariable Long id, @RequestBody UserProduct userProduct) {
        UserProduct existingUserProduct = userProductRepository.findById(id).orElse(null);
        if (existingUserProduct != null) {
            existingUserProduct.setUser(userProduct.getUser());
            existingUserProduct.setProduct(userProduct.getProduct());
            existingUserProduct.setQuantity(userProduct.getQuantity());
            existingUserProduct.setPrice(userProduct.getPrice());
            existingUserProduct.setPurchaseDate(userProduct.getPurchaseDate());
            return userProductRepository.save(existingUserProduct);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteUserProduct(@PathVariable Long id) {
        userProductRepository.deleteById(id);
    }
}
