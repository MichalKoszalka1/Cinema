package com.Cinema.Cinema.repository;

import com.Cinema.Cinema.model.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface FilmRepository  extends JpaRepository<Film,Long> {
    @Query("Select f From Film f Where " + "CONCAT (f.id, ' ' ,f.title , ' ' , f.category , ' ', f.spot)" + "LIKE %?1%")
    public List<Film> findAll(String keyword);
}
