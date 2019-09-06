package ru.javawebinar.topjava.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.model.Vote;
import ru.javawebinar.topjava.to.VoteTo;

import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.topjava.RestaurantTestData.RESTAURANT_1;
import static ru.javawebinar.topjava.UserTestData.USER_1_ID;
import static ru.javawebinar.topjava.VoteTestData.*;

public class VoteServiceTest extends AbstractServiceTest {

    @Autowired
    protected VoteService voteService;

    @Test
    void toVote() throws Exception {
        Vote newVote = new Vote(null, LocalDate.of(2011, 11, 11), USER_1_ID, RESTAURANT_1.getName());
        Vote created = voteService.toVote(newVote);
        newVote.setId(created.getId());
        assertMatch(created, newVote);
        assertMatch(voteService.getHistoryVoteByUser(USER_1_ID), VOTE_7, VOTE_4, VOTE_1, newVote);
        assertMatch(voteService.getAllRestaurantRating(RESTAURANT_1.getName()), VOTE_10, VOTE_7, VOTE_2, VOTE_1, newVote);
    }

    @Test
    void toVoteDuplicate() throws Exception {
        Vote newVote = new Vote(null, LocalDate.of(2019, 7, 21), USER_1_ID, "Северяне");
        Vote created = voteService.toVote(newVote);
        newVote.setId(created.getId());
        assertMatch(created, newVote);
        assertMatch(voteService.getHistoryVoteByUser(USER_1_ID), newVote, VOTE_4, VOTE_1);
        assertMatch(voteService.getAllRestaurantRating("Северяне"), VOTE_8, newVote);
        assertMatch(voteService.getAllRestaurantRating("Горыныч"), VOTE_10, VOTE_2, VOTE_1);
    }

    @Test
    void getAllRestaurantRating() throws Exception {
        List<Vote> all = voteService.getAllRestaurantRating(RESTAURANT_1.getName());
        assertMatch(all, VOTE_10, VOTE_7, VOTE_2, VOTE_1);
    }

    @Test
    void getHistoryVoteByUser() throws Exception {
        List<Vote> all = voteService.getHistoryVoteByUser(USER_1_ID);
        assertMatch(all, VOTE_7, VOTE_4, VOTE_1);
    }

    @Test
    void getRestaurantRating() throws Exception {
        int testRating = voteService.getRestaurantRating(LocalDate.of(2019, 7, 20),"Remy Kitchen Bakery");
        Assertions.assertThat(testRating).isEqualTo(3);
    }

    @Test
    void getRatingByToday() throws Exception {
        int testRating = voteService.getTodayRating("Remy Kitchen Bakery");
        Assertions.assertThat(testRating).isEqualTo(0);
        voteService.toVote(new Vote(null, LocalDate.now(), USER_1_ID, "Remy Kitchen Bakery"));
        testRating = voteService.getTodayRating("Remy Kitchen Bakery");
        Assertions.assertThat(testRating).isEqualTo(1);

    }

    @Test
    void getAllRestaurantRatingGroupByDate() throws Exception {
        List<VoteTo> ratingGroupByDate = voteService.getAllRestaurantRatingGroupByDate("Горыныч");
        Assertions.assertThat(ratingGroupByDate).usingElementComparatorIgnoringFields().isEqualTo(RATING_GROUP_BY_DATE);
    }
}
