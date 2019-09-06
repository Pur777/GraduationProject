package ru.javawebinar.topjava.repository.user;

import ru.javawebinar.topjava.model.User;

import java.util.List;

public interface UserRepository {
    User save(User user);

    boolean delete(int id);

    User get(int id);

    List<User> getAll();

    User update(User user);

    User getByEmail(String email);
}
