package ru.javawebinar.topjava.repository.dish;

import ru.javawebinar.topjava.model.Dish;

import java.util.List;

public interface DishRepository {
    Dish get(int menuId, int id);

    List<Dish> getAll(int menuId);

    Dish save(int menuId, Dish dish);

    boolean delete(int menuId, int id);
}
