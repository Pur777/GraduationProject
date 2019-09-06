package ru.javawebinar.topjava.web.dish;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.topjava.View;
import ru.javawebinar.topjava.model.Dish;
import ru.javawebinar.topjava.service.DishService;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = AdminDishRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminDishRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    public static final String REST_URL = "/rest/admin/dish";

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

    @DeleteMapping("/{dishId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int dishId, @RequestParam int menuId) {
        log.info("delete dish {} for dish {}", dishId, menuId);
        dishService.delete(menuId, dishId);
    }

    @PutMapping(value = "/{dishId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@Validated(View.Web.class) @RequestBody Dish dish, @PathVariable int dishId, @RequestParam int menuId) {
        ValidationUtil.assureIdConsistent(dish, dishId);
        log.info("update {} for dish {}", dish, menuId);
        dishService.update(dish, menuId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@Validated(View.Web.class) @RequestBody Dish dish, @RequestParam int menuId) {
        ValidationUtil.checkNew(dish);
        log.info("create {}", dish);
        Dish created = dishService.create(dish, menuId);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
