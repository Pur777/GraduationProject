package ru.javawebinar.topjava.repository.dish;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Dish;
import ru.javawebinar.topjava.repository.lunchmenu.CrudLunchMenuRepository;

import java.util.List;

@Repository
public class DataJpaDishRepository implements DishRepository {

    @Autowired
    CrudDishRepository dishRepository;

    @Autowired
    CrudLunchMenuRepository menuRepository;

    @Override
    public Dish get(int menuId, int id) {
        return dishRepository.findByMenuIdAndId(menuId, id);
    }

    @Override
    public List<Dish> getAll(int menuId) {
        return dishRepository.findAllByMenuId(menuId);
    }

    @Override
    @Transactional
    public Dish save(int menuId, Dish dish) {
        if (!dish.isNew() && get(menuId, dish.getId()) == null) {
            return null;
        }
        dish.setMenu(menuRepository.getOne(menuId));
        return dishRepository.save(dish);
    }

    @Override
    public boolean delete(int menuId, int id) {
        return dishRepository.delete(menuId, id) != 0;
    }
}
