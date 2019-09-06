package ru.javawebinar.topjava.repository.dish;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Dish;

import java.util.List;

@Transactional(readOnly = true)
public interface CrudDishRepository extends JpaRepository<Dish, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Dish d WHERE d.id=:id AND d.menu.id=:lunchMenuId")
    int delete(@Param("lunchMenuId") int lunchMenuId, @Param("id") int id);

    Dish findByMenuIdAndId(int menuId, int id);

    List<Dish> findAllByMenuId(int menuId);
}
