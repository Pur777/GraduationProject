package ru.javawebinar.topjava.web.menu;

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
import ru.javawebinar.topjava.model.LunchMenu;
import ru.javawebinar.topjava.service.LunchMenuService;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = AdminMenuRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminMenuRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    public static final String REST_URL = "/rest/admin/menu";

    @Autowired
    private LunchMenuService menuService;

    @GetMapping("/{menuId}")
    public LunchMenu get(@PathVariable int menuId, @RequestParam int restaurantId) {
        log.info("get menu {} for restaurant {}", menuId, restaurantId);
        return menuService.get(restaurantId, menuId);
    }

    @GetMapping
    public List<LunchMenu> getAll(@RequestParam int restaurantId) {
        log.info("getAll for restaurant {}", restaurantId);
        return menuService.getAll(restaurantId);
    }

    @GetMapping("/by")
    public LunchMenu getByDate(@RequestParam int restaurantId, @RequestParam String date) {
        log.info("getByDate {} for restaurant {}", date, restaurantId);
        return menuService.getByDate(restaurantId, LocalDate.parse(date));
    }

    @DeleteMapping("/{menuId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int menuId, @RequestParam int restaurantId) {
        log.info("delete menu {} for restaurant {}", menuId, restaurantId);
        menuService.delete(restaurantId, menuId);
    }

    @PutMapping(value = "/{menuId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@Validated(View.Web.class) @RequestBody LunchMenu menu, @PathVariable int menuId, @RequestParam int restaurantId) {
        ValidationUtil.assureIdConsistent(menu, menuId);
        log.info("update {} for restaurant {}", menu, restaurantId);
        menuService.update(menu, restaurantId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LunchMenu> createWithLocation(@Validated(View.Web.class) @RequestBody LunchMenu menu, @RequestParam int restaurantId) {
        ValidationUtil.checkNew(menu);
        log.info("create {}", menu);
        LunchMenu created = menuService.create(menu, restaurantId);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
