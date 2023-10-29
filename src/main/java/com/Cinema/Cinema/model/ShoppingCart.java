package com.Cinema.Cinema.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
@SuppressWarnings( "ALL" )
@Entity
@Table(name = "shoppingcart")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.DATE)
    private Date date;

    @Transient
    private Double totalPrice;

    @Transient
    private int itemNumber;
    @OneToMany(cascade = CascadeType.ALL)
    private Collection<CartItem> items;
    public ShoppingCart(){
        items = new ArrayList<CartItem> ();
    }
    private String sessionToken;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getTotalPrice() {
      Double sum = 0.0;
       for (CartItem item :this.items ){
           sum = sum + item.getFilm ().getPrice ()*item.getQuantity();
       }
        return sum;
    }

    public int getItemNumber() {
        return this.items.size ();
    }

    public Collection<CartItem> getItems() {
        return items;
    }

    public void setItems(Collection<CartItem> items) {
        this.items = items;
    }

    public String getTokenSession() {
        return sessionToken;
    }
    public void setTokenSession(String tokenSession) {
        this.sessionToken = tokenSession;
    }
}
