package ru.javawebinar.topjava.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.javawebinar.topjava.View;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "lunch_menu", uniqueConstraints = {@UniqueConstraint(columnNames = {"date", "restaurant_id"}, name = "lunch_menu_unique")})
public class LunchMenu extends AbstractBaseEntity{

    @Column(name = "date", nullable = false)
    @NotNull
    private LocalDate date;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menu")
    @OrderBy("id")
    private List<Dish> dishes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(groups = View.Persist.class)
    private Restaurant restaurant;

    public LunchMenu() {
    }

    public LunchMenu(LunchMenu lm) {
        this(lm.getId(), lm.getDate(), lm.getDishes());
    }

    public LunchMenu(Integer id, LocalDate date, List<Dish> dishes) {
        super(id);
        this.date = date;
        setDishes(dishes);
    }

    public LunchMenu(Integer id, LocalDate date, Dish... dishes) {
        this(id, date, List.of(dishes));
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
