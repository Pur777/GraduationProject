package ru.javawebinar.topjava.repository.lunchmenu;

import ru.javawebinar.topjava.model.LunchMenu;

import java.time.LocalDate;
import java.util.List;

public interface LunchMenuRepository {
    LunchMenu get(int restaurantId, int id);

    LunchMenu getByDate(int restaurantId, LocalDate date);

    List<LunchMenu> getAll(int restaurantId);

    LunchMenu save(int restaurantId, LunchMenu lunchMenu);

    boolean delete(int restaurantId, int id);

    LunchMenu getLastMenu(int restaurantId);
}
