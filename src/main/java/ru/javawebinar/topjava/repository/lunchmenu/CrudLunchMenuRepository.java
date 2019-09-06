package ru.javawebinar.topjava.repository.lunchmenu;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.LunchMenu;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudLunchMenuRepository extends JpaRepository<LunchMenu, Integer> {

    LunchMenu findByRestaurantIdAndId(int restaurantId, int id);

    LunchMenu findByRestaurantIdAndDate(int restaurantId, LocalDate date);

    LunchMenu findFirstByRestaurantId(int restaurantId, Sort sort);

    List<LunchMenu> findAllByRestaurantId(int restaurantId, Sort sort);

    @Modifying
    @Transactional
    @Query("DELETE FROM LunchMenu lm WHERE lm.id=:id AND lm.restaurant.id=:restaurantId")
    int delete(@Param("restaurantId") int restaurantId, @Param("id") int id);
}
