package com.example.onlinestore.repository;

import com.example.onlinestore.model.ShoppingCart;
import com.example.onlinestore.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ShoppingCartRepository extends CrudRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findByUser(User user);
}

