package ru.javawebinar.topjava.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;

import javax.validation.Valid;
import java.net.URI;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(ProfileRestController.REST_URL)
public class ProfileRestController {
    public static final String REST_URL = "/rest/profile";

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UniqueMailValidator emailValidator;

    @Autowired
    protected UserService userService;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public User get(@AuthenticationPrincipal AuthorizedUser authUser) {
        int id = authUser.getId();
        log.info("get {}", id);
        return userService.get(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthorizedUser authUser) {
        int id = authUser.getId();
        log.info("delete {}", id);
        userService.delete(id);
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        log.info("create {}", user);
        checkNew(user);
        User created = userService.create(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody User user, @AuthenticationPrincipal AuthorizedUser authUser) {
        int id = authUser.getId();
        log.info("update {} with id={}", user, id);
        assureIdConsistent(user, id);
        userService.update(user);
    }
}
