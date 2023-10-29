package com.Cinema.Cinema.repository;

import com.Cinema.Cinema.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart,Long> {
    ShoppingCart findBySessionToken(String sessionToken);
}
