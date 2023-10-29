package com.Cinema.Cinema.repository;

import com.Cinema.Cinema.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
}
