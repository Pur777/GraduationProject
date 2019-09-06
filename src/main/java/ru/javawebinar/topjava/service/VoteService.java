package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.javawebinar.topjava.model.Vote;
import ru.javawebinar.topjava.repository.vote.VoteRepository;
import ru.javawebinar.topjava.to.VoteTo;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VoteService {
    private final VoteRepository voteRepository;

    @Autowired
    public VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public Vote toVote(Vote vote) {
        Assert.notNull(vote, "vote must not be null");
        voteRepository.delete(vote.getDate(), vote.getUserId());
        return voteRepository.save(vote);
    }

    public Integer getRestaurantRating(LocalDate date, String restaurantName) {
        Assert.notNull(date, "date must not be null");
        Assert.notNull(restaurantName, "name must not be null");
        return voteRepository.getRestaurantRating(date, restaurantName);
    }

    public Integer getTodayRating(String restaurantName) {
        Assert.notNull(restaurantName, "name must not be null");
        return voteRepository.getRestaurantRating(LocalDate.now(), restaurantName);
    }

    public List<Vote> getAllRestaurantRating(String restaurantName) {
        Assert.notNull(restaurantName, "name must not be null");
        return voteRepository.getAllRestaurantRating(restaurantName);
    }

    public List<VoteTo> getAllRestaurantRatingGroupByDate(String restaurantName) {
        Assert.notNull(restaurantName, "name must not be null");
        return voteRepository.getAllRestaurantRating(restaurantName).stream()
                .collect(Collectors.toMap(Vote::getDate, vote -> 1, Integer::sum))
                .entrySet().stream()
                .map(entry -> new VoteTo(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public List<Vote> getHistoryVoteByUser(int userId) {
        return voteRepository.getHistoryVoteByUser(userId);
    }
}
