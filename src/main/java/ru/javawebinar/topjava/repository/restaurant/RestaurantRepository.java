package ru.javawebinar.topjava.repository.restaurant;

import ru.javawebinar.topjava.model.Restaurant;

import java.util.List;

public interface RestaurantRepository {

    Restaurant get(int id);

    Restaurant getByName(String name);

    List<Restaurant> getAll();

    Restaurant save(Restaurant restaurant);

    boolean delete(int id);
}
