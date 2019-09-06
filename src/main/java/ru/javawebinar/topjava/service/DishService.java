package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.javawebinar.topjava.model.Dish;
import ru.javawebinar.topjava.repository.dish.DishRepository;

import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class DishService {

    private final DishRepository dishRepository;

    @Autowired
    public DishService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    public Dish get(int menuId, int id) {
        return checkNotFoundWithId(dishRepository.get(menuId, id), id);
    }

    public List<Dish> getAll(int menuId) {
        return dishRepository.getAll(menuId);
    }

    public void update(Dish dish, int menuId) {
        Assert.notNull(dish, "dish must not be null");
        checkNotFoundWithId(dishRepository.save(menuId, dish), dish.getId());
    }

    public Dish create(Dish dish, int restaurantId) {
        Assert.notNull(dish, "dish must not be null");
        return dishRepository.save(restaurantId, dish);
    }

    public void delete(int menuId, int id) {
        checkNotFoundWithId(dishRepository.delete(menuId, id), id);
    }
}
