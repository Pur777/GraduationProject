package ru.javawebinar.topjava.web.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.javawebinar.topjava.model.LunchMenu;
import ru.javawebinar.topjava.service.LunchMenuService;

@RestController
@RequestMapping(value = ProfileMenuRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileMenuRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    public static final String REST_URL = "/rest/profile/menu";

    @Autowired
    private LunchMenuService menuService;

    @GetMapping("/last")
    public LunchMenu getLastMenu(@RequestParam int restaurantId) {
        log.info("getLastMenu for restaurant {}", restaurantId);
        return menuService.getLastMenu(restaurantId);
    }
}
