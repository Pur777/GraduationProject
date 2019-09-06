package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.javawebinar.topjava.model.LunchMenu;
import ru.javawebinar.topjava.repository.lunchmenu.LunchMenuRepository;

import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFound;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class LunchMenuService {

    private final LunchMenuRepository menuRepository;

    @Autowired
    public LunchMenuService(LunchMenuRepository repository) {
        this.menuRepository = repository;
    }

    public LunchMenu get(int restaurantId, int id) {
        return checkNotFoundWithId(menuRepository.get(restaurantId, id), id);
    }

    public LunchMenu getByDate(int restaurantId, LocalDate date) {
        Assert.notNull(date, "date must not be null");
        return checkNotFound(menuRepository.getByDate(restaurantId, date), "date=" + date);
    }

    public LunchMenu getLastMenu(int restaurantId) {
        return checkNotFound(menuRepository.getLastMenu(restaurantId), "restaurant ID=" + restaurantId);
    }

    public List<LunchMenu> getAll(int restaurantId) {
        return menuRepository.getAll(restaurantId);
    }

    public void update(LunchMenu menu, int restaurantId) {
        Assert.notNull(menu, "menu must not be null");
        checkNotFoundWithId(menuRepository.save(restaurantId, menu), menu.getId());
    }

    public LunchMenu create(LunchMenu menu, int restaurantId) {
        Assert.notNull(menu, "menu must not be null");
        return menuRepository.save(restaurantId, menu);
    }

    public void delete(int restaurantId, int id) {
        checkNotFoundWithId(menuRepository.delete(restaurantId, id), id);
    }
}
