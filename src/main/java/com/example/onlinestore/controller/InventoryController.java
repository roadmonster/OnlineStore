package com.example.onlinestore.controller;

import com.example.onlinestore.model.Inventory;
import com.example.onlinestore.model.Product;
import com.example.onlinestore.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    @Autowired
    private InventoryRepository inventoryRepository;

    @PostMapping
    public Inventory createInventory(@RequestBody Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    @GetMapping("/{id}")
    public Inventory getInventoryById(@PathVariable Long id) {
        return inventoryRepository.findById(id).orElse(null);
    }

    @GetMapping("/product/{productId}")
    public Inventory getInventoryByProductId(@PathVariable Long productId) {
        Product product = new Product();
        product.setId(productId);
        return inventoryRepository.findByProduct(product).orElse(null);
    }

    @PutMapping("/{id}")
    public Inventory updateInventory(@PathVariable Long id, @RequestBody Inventory inventory) {
        Inventory existingInventory = inventoryRepository.findById(id).orElse(null);
        if (existingInventory != null) {
            existingInventory.setProduct(inventory.getProduct());
            existingInventory.setQuantity(inventory.getQuantity());
            return inventoryRepository.save(existingInventory);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteInventory(@PathVariable Long id) {
        inventoryRepository.deleteById(id);
    }
}


