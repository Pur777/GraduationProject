package ru.javawebinar.topjava.model;

import org.hibernate.validator.constraints.SafeHtml;
import ru.javawebinar.topjava.View;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "restaurants", uniqueConstraints = {@UniqueConstraint(columnNames = "name", name = "restaurants_unique_name")})
public class Restaurant extends AbstractBaseEntity{

    @NotBlank
    @Size(min = 2, max = 100)
    @SafeHtml(groups = {View.Web.class})
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OrderBy("dateTime DESC")
    private List<LunchMenu> menu;

    public Restaurant() {
    }

    public Restaurant(Integer id, String name) {
        super(id);
        this.name = name;
    }

    public Restaurant(Restaurant restaurant) {
        super(restaurant.id);
        this.name = restaurant.name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<LunchMenu> getMenu() {
        return menu;
    }

    public void setMenu(List<LunchMenu> menu) {
        this.menu = menu;
    }
}
