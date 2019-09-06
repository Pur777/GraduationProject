package ru.javawebinar.topjava.repository.vote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Vote;

import java.time.LocalDate;
import java.util.List;

@Repository
public class DataJpaVoteRepository implements VoteRepository{

    @Autowired
    protected CrudVoteRepository voteRepository;

    @Override
    @Transactional
    public Vote save(Vote vote) {
        return voteRepository.save(vote);
    }

    @Override
    public boolean delete(LocalDate date, int userId) {
        return voteRepository.delete(date, userId) != 0;
    }

    @Override
    public Integer getRestaurantRating(LocalDate date, String restaurantName) {
        return voteRepository.countAllByDateAndRestaurantName(date, restaurantName);
    }

    @Override
    public List<Vote> getAllRestaurantRating(String restaurantName) {
        return voteRepository.findAllByRestaurantNameOrderByDateDesc(restaurantName);
    }

    @Override
    public List<Vote> getHistoryVoteByUser(int userId) {
        return voteRepository.findAllByUserIdOrderByDateDesc(userId);
    }
}
