package ru.javawebinar.topjava.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.javawebinar.topjava.model.LunchMenu;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.topjava.MenuAndDishTestData.assertMatch;
import static ru.javawebinar.topjava.MenuAndDishTestData.*;
import static ru.javawebinar.topjava.RestaurantTestData.*;

public class LunchMenuServiceTest extends AbstractServiceTest {

    @Autowired
    protected LunchMenuService menuService;

    @Autowired
    private DishService dishService;

    @Test
    void create() throws Exception {
        LunchMenu newMenu = new LunchMenu(null, LocalDate.of(2016, 2, 28));
        LunchMenu created = menuService.create(newMenu, RESTAURANT_ID_2);
        newMenu.setId(created.getId());
        assertMatch(created, newMenu);
        assertMatch(menuService.getAll(RESTAURANT_ID_2), newMenu, LUNCH_MENU_2);
    }

    @Test
    void duplicateDateCreate() throws Exception {
        assertThrows(DataAccessException.class, () ->
                menuService.create(new LunchMenu(null, LocalDate.of(2015, 5, 30)), RESTAURANT_ID_1));
    }

    @Test
    void delete() throws Exception {
        menuService.delete(RESTAURANT_ID_1, LUNCH_MENU_ID_1);
        assertMatch(menuService.getAll(RESTAURANT_ID_1), LUNCH_MENU_5);
    }

    @Test
    void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> menuService.delete(RESTAURANT_ID_3, 1));
        assertThrows(NotFoundException.class, () -> menuService.delete(1, LUNCH_MENU_ID_4));
        assertThrows(NotFoundException.class, () -> menuService.delete(1, 2));
    }

    @Test
    void get() throws Exception {
        LunchMenu menu = menuService.get(RESTAURANT_ID_4, LUNCH_MENU_ID_4);
        assertMatch(menu, LUNCH_MENU_4);
        assertMatchDish(dishService.getAll(menu.getId()), LUNCH_MENU_4.getDishes());
    }

    @Test
    void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> menuService.get(RESTAURANT_ID_1, 1));
        assertThrows(NotFoundException.class, () -> menuService.get(1, LUNCH_MENU_ID_1));
        assertThrows(NotFoundException.class, () -> menuService.get(1, 2));
    }

    @Test
    void getByDate() throws Exception {
        LunchMenu menu = menuService.getByDate(RESTAURANT_ID_1, LocalDate.of(2016, 2, 3));
        assertMatch(menu, LUNCH_MENU_5);
        assertMatchDish(dishService.getAll(menu.getId()), LUNCH_MENU_5.getDishes());
    }

    @Test
    void getByDateNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> menuService.getByDate(RESTAURANT_ID_1, LocalDate.of(2017, 4, 24)));
        assertThrows(NotFoundException.class, () -> menuService.getByDate(1, LocalDate.of(2016, 2, 3)));
        assertThrows(NotFoundException.class, () -> menuService.getByDate(1, LocalDate.of(2017, 4, 24)));
    }

    @Test
    void getLastMenu() throws Exception {
        LunchMenu menu = menuService.getLastMenu(RESTAURANT_ID_1);
        assertMatch(menu, LUNCH_MENU_5);
        assertMatchDish(dishService.getAll(menu.getId()), LUNCH_MENU_5.getDishes());
    }

    @Test
    void update() throws Exception {
        LunchMenu updated = new LunchMenu(LUNCH_MENU_2);
        updated.setDate(LocalDate.of(2019, 8, 13));
        menuService.update(updated, RESTAURANT_ID_2);
        LunchMenu test = menuService.get(RESTAURANT_ID_2, LUNCH_MENU_ID_2);
        assertMatch(test, updated);
        assertMatchDish(dishService.getAll(test.getId()), updated.getDishes());
    }

    @Test
    void getAll() throws Exception {
        List<LunchMenu> all = menuService.getAll(RESTAURANT_ID_1);
        assertMatch(all, LUNCH_MENU_5, LUNCH_MENU_1);
    }
}
