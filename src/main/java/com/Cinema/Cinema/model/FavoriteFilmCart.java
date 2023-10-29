package com.Cinema.Cinema.model;

import javax.persistence.*;
import java.util.*;
@SuppressWarnings( "ALL" )
@Entity
@Table(name = "favoritecart")
public class FavoriteFilmCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.DATE)
    private Date date;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER )
    private Collection<FavoriteFilmItem> items = new ArrayList<FavoriteFilmItem> ();

    @Transient
    private int itemNumber;
    public FavoriteFilmCart(){
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

    public int getItemNumber() {
        return this.items.size ();
    }

    public Collection<FavoriteFilmItem> getItems() {
        return items;
    }

    public void setItems(Collection<FavoriteFilmItem> items) {
        this.items = items;
    }

    public String getSessionToken() {
        return sessionToken;
    }
    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }
}
