package ru.javawebinar.topjava.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.TestUtil;
import ru.javawebinar.topjava.web.vote.AdminVoteRestController;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.RestaurantTestData.RESTAURANT_1;
import static ru.javawebinar.topjava.TestUtil.userHttpBasic;
import static ru.javawebinar.topjava.UserTestData.ADMIN;
import static ru.javawebinar.topjava.UserTestData.USER_1;
import static ru.javawebinar.topjava.VoteTestData.*;

public class AdminVoteRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = AdminVoteRestController.REST_URL + '/';

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
}
