package ru.javawebinar.topjava.web.dish;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Dish;
import ru.javawebinar.topjava.service.DishService;

import java.util.List;

@RestController
@RequestMapping(value = ProfileDishRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileDishRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    public static final String REST_URL = "/rest/profile/dish";

    @Autowired
    private DishService dishService;

    @GetMapping("/{dishId}")
    public Dish get(@PathVariable int dishId, @RequestParam int menuId) {
        log.info("get dish {} for menu {}", dishId, menuId);
        return dishService.get(menuId, dishId);
    }

    @GetMapping
    public List<Dish> getAll(@RequestParam int menuId) {
        log.info("getAll for dish {}", menuId);
        return dishService.getAll(menuId);
    }
}
