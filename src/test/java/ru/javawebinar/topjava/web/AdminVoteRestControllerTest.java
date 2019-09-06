package ru.javawebinar.topjava.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.TestUtil;
import ru.javawebinar.topjava.model.Vote;
import ru.javawebinar.topjava.service.VoteService;
import ru.javawebinar.topjava.web.json.JsonUtil;
import ru.javawebinar.topjava.web.vote.AdminVoteRestController;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.RestaurantTestData.RESTAURANT_1;
import static ru.javawebinar.topjava.TestUtil.readFromJson;
import static ru.javawebinar.topjava.TestUtil.userHttpBasic;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.VoteTestData.assertMatch;
import static ru.javawebinar.topjava.VoteTestData.contentJson;
import static ru.javawebinar.topjava.VoteTestData.*;
import static ru.javawebinar.topjava.util.ValidationUtil.TIME_TO_VOTE;
import static ru.javawebinar.topjava.util.exception.ErrorType.TIME_ERROR;

public class AdminVoteRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = AdminVoteRestController.REST_URL + '/';

    @Autowired
    private VoteService voteService;

    @Test
    void getRestaurantRating() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "ratingByDate")
                .param("restaurantName", RESTAURANT_1.getName())
                .param("date", "2019-07-19")
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertMatchRating(TestUtil.readFromJsonMvcResult(result, Integer.class), 2));
    }

    @Test
    void getTodayRating() throws Exception {
        voteService.toVote(new Vote(null, LocalDate.now(), USER_1_ID, RESTAURANT_1.getName()));
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "todayRating")
                .param("restaurantName", RESTAURANT_1.getName())
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertMatchRating(TestUtil.readFromJsonMvcResult(result, Integer.class), 1));
    }

    @Test
    void getAllRestaurantRating() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL)
                .param("restaurantName", RESTAURANT_1.getName())
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(VOTE_10, VOTE_7, VOTE_2, VOTE_1));
    }

    @Test
    void getHistoryVoteByUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "byUser")
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(VOTE_9, VOTE_6, VOTE_3));
    }

    @Test
    void getAllRestaurantRatingGroupByDate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "ratingGroupByDate")
                .param("restaurantName", RESTAURANT_1.getName())
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJsonTo(RATING_GROUP_BY_DATE));
    }

    @Test
    void getUnauth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "ratingByDate")
                .param("restaurantName", RESTAURANT_1.getName())
                .param("date", "2019-07-19"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "ratingByDate")
                .param("restaurantName", RESTAURANT_1.getName())
                .param("date", "2019-07-19")
                .with(userHttpBasic(USER_1)))
                .andExpect(status().isForbidden());
    }

    @Test
    void toVote() throws Exception {
        if (LocalTime.now().isBefore(TIME_TO_VOTE)) {

            Vote created = new Vote(null, LocalDate.now(), ADMIN_ID, RESTAURANT_1.getName());
            ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(created))
                    .with(userHttpBasic(ADMIN)));

            Vote returned = readFromJson(action, Vote.class);
            created.setId(returned.getId());

            assertMatch(returned, created);
            assertMatch(voteService.getAllRestaurantRating(RESTAURANT_1.getName()), created, VOTE_10, VOTE_7, VOTE_2, VOTE_1);
        }
    }

    @Test
    void toVoteAfterEleven() throws Exception {
        if (LocalTime.now().isAfter(TIME_TO_VOTE)) {

            Vote invalid = new Vote(null, LocalDate.now(), ADMIN_ID, RESTAURANT_1.getName());
            mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(invalid))
                    .with(userHttpBasic(ADMIN)))
                    .andDo(print())
                    .andExpect(status().isConflict())
                    .andExpect(errorType(TIME_ERROR))
                    .andDo(print());
        }
    }
}
