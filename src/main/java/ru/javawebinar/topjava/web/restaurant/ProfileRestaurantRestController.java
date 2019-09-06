package ru.javawebinar.topjava.web.restaurant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Restaurant;
import ru.javawebinar.topjava.service.RestaurantService;

import java.util.List;

@RestController
@RequestMapping(value = ProfileRestaurantRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileRestaurantRestController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    public static final String REST_URL = "/rest/profile/restaurants";

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/{id}")
    public Restaurant get(@PathVariable int id) {
        log.info("get restaurant {}", id);
        return restaurantService.get(id);
    }

    @GetMapping
    public List<Restaurant> getAll() {
        log.info("getAll");
        return restaurantService.getAll();
    }

    @GetMapping("/by")
    public Restaurant getByName(@RequestParam String name) {
        log.info("getByName {}", name);
        return restaurantService.getByName(name);
    }
}
