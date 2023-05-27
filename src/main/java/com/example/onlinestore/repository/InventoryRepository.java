package com.example.onlinestore.repository;

import com.example.onlinestore.model.Inventory;
import com.example.onlinestore.model.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface InventoryRepository extends CrudRepository<Inventory, Long> {
    Optional<Inventory> findByProduct(Product product);
}

