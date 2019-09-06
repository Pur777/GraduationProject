package ru.javawebinar.topjava.repository.vote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Vote;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudVoteRepository extends JpaRepository<Vote, Integer> {
    List<Vote> findAllByRestaurantNameOrderByDateDesc(String restaurantName);

    List<Vote> findAllByUserIdOrderByDateDesc(int userId);

    Integer countAllByDateAndRestaurantName(LocalDate date, String restaurantName);

    @Modifying
    @Transactional
    @Query("DELETE FROM Vote v WHERE v.date=:date AND v.userId=:userId")
    int delete(@Param("date") LocalDate date, @Param("userId") int userId);
}
