package ru.javawebinar.topjava.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.model.Dish;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.topjava.MenuAndDishTestData.*;

public class DishServiceTest extends AbstractServiceTest {

    @Autowired
    protected DishService dishService;

    @Test
    void create() throws Exception {
        Dish newDish = new Dish(null, "Бургер", 150.99);
        Dish created = dishService.create(newDish, LUNCH_MENU_ID_1);
        newDish.setId(created.getId());
        assertMatchDish(created, newDish);
        assertMatchDish(dishService.getAll(LUNCH_MENU_ID_1), DISH_1, DISH_2, newDish);
    }

    @Test
    void update() throws Exception {
        Dish updated = new Dish(DISH_5);
        updated.setName("Бургер");
        dishService.update(updated, LUNCH_MENU_ID_3);
        Dish test = dishService.get(LUNCH_MENU_ID_3, DISH_ID_5);
        assertMatchDish(test, updated);
    }

    @Test
    void delete() throws Exception {
        dishService.delete(LUNCH_MENU_ID_1, DISH_ID_2);
        assertMatchDish(dishService.getAll(LUNCH_MENU_ID_1), DISH_1);
    }

    @Test
    void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> dishService.delete(LUNCH_MENU_ID_3, 1));
        assertThrows(NotFoundException.class, () -> dishService.delete(1, DISH_ID_5));
        assertThrows(NotFoundException.class, () -> dishService.delete(1, 2));
    }

    @Test
    void get() throws Exception {
        Dish dish = dishService.get(LUNCH_MENU_ID_4, DISH_ID_8);
        assertMatchDish(dish, DISH_8);
    }

    @Test
    void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> dishService.get(LUNCH_MENU_ID_4, 1));
        assertThrows(NotFoundException.class, () -> dishService.get(1, DISH_ID_7));
        assertThrows(NotFoundException.class, () -> dishService.get(1, 2));
    }

    @Test
    void getAll() throws Exception {
        List<Dish> all = dishService.getAll(LUNCH_MENU_ID_2);
        assertMatchDish(all, DISH_3, DISH_4);
    }

    @Test
    void createWithException() throws Exception {
        validateRootCause(() -> dishService.create(new Dish(null, "  ", 44.5), LUNCH_MENU_ID_3), ConstraintViolationException.class);
        validateRootCause(() -> dishService.create(new Dish(null, "Бургер", -44.5), LUNCH_MENU_ID_3), ConstraintViolationException.class);
    }
}
