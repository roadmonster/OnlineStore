package com.example.onlinestore.repository;

import com.example.onlinestore.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
}

