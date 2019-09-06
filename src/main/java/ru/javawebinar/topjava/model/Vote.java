package ru.javawebinar.topjava.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "vote", uniqueConstraints = {@UniqueConstraint(columnNames = {"date", "user_id"}, name = "vote_unique")})
public class Vote extends AbstractBaseEntity{

    @Column(name = "date", nullable = false)
    @NotNull
    private LocalDate date;

    @Column(name = "user_id", nullable = false)
    @NotNull
    private Integer userId;

    @NotBlank
    @Size(min = 2, max = 100)
    @Column(name = "restaurant_name", nullable = false)
    private String restaurantName;

    public Vote() {
    }

    public Vote(Integer id, LocalDate date, Integer userId, String restaurantName) {
        super(id);
        this.date = date;
        this.userId = userId;
        this.restaurantName = restaurantName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }
}
