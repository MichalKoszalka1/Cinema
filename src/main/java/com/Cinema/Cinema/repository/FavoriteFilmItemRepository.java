package com.Cinema.Cinema.repository;

import com.Cinema.Cinema.model.FavoriteFilmItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface FavoriteFilmItemRepository extends JpaRepository<FavoriteFilmItem,Long> {
}
