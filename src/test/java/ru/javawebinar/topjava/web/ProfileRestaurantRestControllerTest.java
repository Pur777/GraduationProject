package ru.javawebinar.topjava.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.TestUtil;
import ru.javawebinar.topjava.model.Restaurant;
import ru.javawebinar.topjava.web.restaurant.ProfileRestaurantRestController;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.RestaurantTestData.assertMatch;
import static ru.javawebinar.topjava.RestaurantTestData.contentJson;
import static ru.javawebinar.topjava.RestaurantTestData.*;
import static ru.javawebinar.topjava.TestUtil.userHttpBasic;
import static ru.javawebinar.topjava.UserTestData.*;

public class ProfileRestaurantRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = ProfileRestaurantRestController.REST_URL + '/';

    @Test
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT_ID_1)
                .with(userHttpBasic(USER_2)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertMatch(TestUtil.readFromJsonMvcResult(result, Restaurant.class), RESTAURANT_1));
    }

    @Test
    void getByName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "by?name=" + RESTAURANT_1.getName())
                .with(userHttpBasic(USER_1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertMatch(TestUtil.readFromJsonMvcResult(result, Restaurant.class), RESTAURANT_1));
    }

    @Test
    void getUnauth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT_ID_1))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(RESTAURANT_4, RESTAURANT_3, RESTAURANT_1, RESTAURANT_2));
    }
}
