package com.Cinema.Cinema.model;

import javax.persistence.*;
import java.util.Date;
@Entity
@Table(name = "cartitem")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal (TemporalType.DATE)
    private Date date;
    private int quantity;

    @ManyToOne(fetch = FetchType.EAGER)
    private Film film;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {

        this.quantity = quantity;
    }
    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartItem)) return false;

        CartItem cartItem = (CartItem) o;

        return getId ( ).equals (cartItem.getId ( ));
    }
    @Override
    public int hashCode() {
        return getId ( ).hashCode ( );
    }
}
