package ru.javawebinar.topjava.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.SafeHtml;
import ru.javawebinar.topjava.View;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Entity
@Table(name = "dishes")
public class Dish extends AbstractBaseEntity{

    @NotBlank
    @Size(min = 2, max = 100)
    @SafeHtml(groups = {View.Web.class})
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    @NotNull
    @PositiveOrZero
    private Double price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lunch_menu_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(groups = View.Persist.class)
    private LunchMenu menu;

    public Dish() {
    }

    public Dish(Integer id, String name, Double price) {
        super(id);
        this.name = name;
        this.price = price;
    }

    public Dish(Dish d) {
        this(d.getId(), d.getName(), d.getPrice());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LunchMenu getMenu() {
        return menu;
    }

    public void setMenu(LunchMenu menu) {
        this.menu = menu;
    }
}
