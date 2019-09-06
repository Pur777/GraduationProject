package ru.javawebinar.topjava.repository.vote;

import ru.javawebinar.topjava.model.Vote;

import java.time.LocalDate;
import java.util.List;

public interface VoteRepository {

    Vote save(Vote vote);

    boolean delete(LocalDate date, int userId);

    Integer getRestaurantRating(LocalDate date, String restaurantName);

    List<Vote> getAllRestaurantRating(String restaurantName);

    List<Vote> getHistoryVoteByUser(int userId);
}
