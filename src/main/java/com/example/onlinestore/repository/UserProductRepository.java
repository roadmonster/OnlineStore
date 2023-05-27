package com.example.onlinestore.repository;

import com.example.onlinestore.model.UserProduct;
import org.springframework.data.repository.CrudRepository;

public interface UserProductRepository extends CrudRepository<UserProduct, Long> {
}

