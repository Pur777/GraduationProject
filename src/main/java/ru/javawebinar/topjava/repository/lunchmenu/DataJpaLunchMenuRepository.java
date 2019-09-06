package ru.javawebinar.topjava.repository.lunchmenu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.LunchMenu;
import ru.javawebinar.topjava.repository.restaurant.CrudRestaurantRepository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class DataJpaLunchMenuRepository implements LunchMenuRepository {
    private static final Sort SORT_DATE = new Sort(Sort.Direction.DESC, "date");


    @Autowired
    CrudLunchMenuRepository menuRepository;

    @Autowired
    CrudRestaurantRepository restaurantRepository;

    @Override
    public LunchMenu get(int restaurantId, int id) {
        return menuRepository.findByRestaurantIdAndId(restaurantId, id);
    }

    @Override
    public LunchMenu getByDate(int restaurantId, LocalDate date) {
        return menuRepository.findByRestaurantIdAndDate(restaurantId, date);
    }

    @Override
    public List<LunchMenu> getAll(int restaurantId) {
        return menuRepository.findAllByRestaurantId(restaurantId, SORT_DATE);
    }

    @Override
    @Transactional
    public LunchMenu save(int restaurantId, LunchMenu lunchMenu) {
        if (!lunchMenu.isNew() && get(restaurantId, lunchMenu.getId()) == null) {
            return null;
        }
        lunchMenu.setRestaurant(restaurantRepository.findById(restaurantId).orElse(null));
        return menuRepository.save(lunchMenu);
    }

    @Override
    public boolean delete(int restaurantId, int id) {
        return menuRepository.delete(restaurantId, id) != 0;
    }

    @Override
    public LunchMenu getLastMenu(int restaurantId) {
        LunchMenu lunchMenu = menuRepository.findFirstByRestaurantId(restaurantId, SORT_DATE);
        if (lunchMenu == null) {
            lunchMenu = new LunchMenu(null, LocalDate.now(), List.of());
            save(restaurantId, lunchMenu);
        }
        return lunchMenu;
    }
}
