package com.Cinema.Cinema.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import java.sql.Date;

@SuppressWarnings( "ALL" )
@Data
@Entity
@Table(name = "film")
public class Film {
    @Id
    @Column(name = "film_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title",nullable = false)
    private String title;
    @Column(name = "description" ,nullable = false,length = 2000)
    private String description;
    @Column(name = "price",nullable = false)
    private Double price;
    @Column(name = "spot")
    private String spot;
    @Column(name = "category", nullable = false)
    private String category;
    @Column( name = "ticket" )
    private int ticket;
    @Column(name = "date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.sql.Date date;
    @Column(name = "time")
    private String time;
    @Column(name = "duration")
    private int duration;
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB" )
    private String image;
    @Column(name = "age")
    private int age;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getSpot() {
        return spot;
    }
    public void setSpot(String spot) {
        this.spot = spot;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public int getTicket() {
        return ticket;
    }
    public void setTicket(int ticket) {
        this.ticket = ticket;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", spot='" + spot + '\'' +
                ", category='" + category + '\'' +
                ", availableTicket=" + ticket +
                ", date=" + date +
                ", time='" + time + '\'' +
                ", duration=" + duration +
                ", image='" + image + '\'' +
                ", age=" + age +
                '}';
    }


}
