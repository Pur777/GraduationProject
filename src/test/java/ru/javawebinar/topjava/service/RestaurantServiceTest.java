package ru.javawebinar.topjava.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.javawebinar.topjava.model.Restaurant;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.topjava.RestaurantTestData.*;

public class RestaurantServiceTest extends AbstractServiceTest {

    @Autowired
    protected RestaurantService restaurantService;

    @Test
    void create() throws Exception {
        Restaurant newRestaurant = new Restaurant(null, "New");
        Restaurant created = restaurantService.create(new Restaurant(newRestaurant));
        newRestaurant.setId(created.getId());
        assertMatch(created, newRestaurant);
        assertMatch(restaurantService.getAll(), newRestaurant, RESTAURANT_4, RESTAURANT_3, RESTAURANT_1, RESTAURANT_2);
    }

    @Test
    void duplicateNameCreate() throws Exception {
        assertThrows(DataAccessException.class, () ->
                restaurantService.create(new Restaurant(null, "Горыныч")));
    }

    @Test
    void delete() throws Exception {
        restaurantService.delete(RESTAURANT_ID_2);
        assertMatch(restaurantService.getAll(), RESTAURANT_4, RESTAURANT_3, RESTAURANT_1);
    }

    @Test
    void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                restaurantService.delete(1));
    }

    @Test
    void get() throws Exception {
        Restaurant restaurant = restaurantService.get(RESTAURANT_ID_3);
        assertMatch(restaurant, RESTAURANT_3);
    }

    @Test
    void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                restaurantService.get(1));
    }

    @Test
    void getByName() throws Exception {
        Restaurant restaurant = restaurantService.getByName("Remy Kitchen Bakery");
        assertMatch(restaurant, RESTAURANT_4);
    }

    @Test
    void getByEmailNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                restaurantService.getByName("Not Found"));
    }

    @Test
    void update() throws Exception {
        Restaurant updated = new Restaurant(RESTAURANT_1);
        updated.setName("UpdatedName");
        restaurantService.update(updated);
        assertMatch(restaurantService.get(RESTAURANT_ID_1), updated);
    }

    @Test
    void getAll() throws Exception {
        List<Restaurant> all = restaurantService.getAll();
        assertMatch(all, RESTAURANT_4, RESTAURANT_3, RESTAURANT_1, RESTAURANT_2);
    }

    @Test
    void createWithException() throws Exception {
        validateRootCause(() -> restaurantService.create(new Restaurant(null, "  ")), ConstraintViolationException.class);
    }
}
