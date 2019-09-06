package ru.javawebinar.topjava.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.topjava.UserTestData.*;

public class UserServiceTest extends AbstractServiceTest{

    @Autowired
    protected UserService userService;

    @Test
    void create() throws Exception {
        User newUser = new User(null, "New", "new@gmail.com", "newPass", Collections.singleton(Role.ROLE_USER));
        User created = userService.create(new User(newUser));
        newUser.setId(created.getId());
        assertMatch(created, newUser);
        assertMatch(userService.getAll(), ADMIN, newUser, USER_2, USER_1);
    }

    @Test
    void duplicateMailCreate() throws Exception {
        assertThrows(DataAccessException.class, () ->
                userService.create(new User(null, "Duplicate", "user@mail.ru", "newPass", Role.ROLE_USER)));
    }

    @Test
    void delete() throws Exception {
        userService.delete(USER_2_ID);
        assertMatch(userService.getAll(), ADMIN, USER_1);
    }

    @Test
    void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                userService.delete(1));
    }

    @Test
    void get() throws Exception {
        User user = userService.get(ADMIN_ID);
        assertMatch(user, ADMIN);
    }

    @Test
    void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                userService.get(1));
    }

    @Test
    void getByEmail() throws Exception {
        User user = userService.getByEmail("admin@gmail.com");
        assertMatch(user, ADMIN);
    }

    @Test
    void update() throws Exception {
        User updated = new User(USER_1);
        updated.setName("UpdatedName");
        updated.setRoles(Collections.singletonList(Role.ROLE_ADMIN));
        userService.update(updated);
        assertMatch(userService.get(USER_1_ID), updated);
    }

    @Test
    void getAll() throws Exception {
        List<User> all = userService.getAll();
        assertMatch(all, ADMIN, USER_2, USER_1);
    }

    @Test
    void createWithException() throws Exception {
        validateRootCause(() -> userService.create(new User(null, "  ", "mail@yandex.ru", "password",  Role.ROLE_USER)), ConstraintViolationException.class);
        validateRootCause(() -> userService.create(new User(null, "User", "  ", "password", Role.ROLE_USER)), ConstraintViolationException.class);
        validateRootCause(() -> userService.create(new User(null, "User", "mail@yandex.ru", "  ", Role.ROLE_USER)), ConstraintViolationException.class);
    }
}
