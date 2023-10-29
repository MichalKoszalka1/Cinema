package com.Cinema.Cinema.repository;

import com.Cinema.Cinema.model.FavoriteFilmCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface FavoriteFilmCartRepository extends JpaRepository<FavoriteFilmCart,Long> {
    FavoriteFilmCart findBySessionToken(String sessionToken);
}
