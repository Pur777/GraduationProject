package ru.javawebinar.topjava.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.TestUtil;
import ru.javawebinar.topjava.model.Dish;
import ru.javawebinar.topjava.web.dish.ProfileDishRestController;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MenuAndDishTestData.*;
import static ru.javawebinar.topjava.TestUtil.userHttpBasic;
import static ru.javawebinar.topjava.UserTestData.*;

public class ProfileDishRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = ProfileDishRestController.REST_URL + '/';

    @Test
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + DISH_ID_1)
                .param("menuId", String.valueOf(LUNCH_MENU_ID_1))
                .with(userHttpBasic(USER_1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertMatchDish(TestUtil.readFromJsonMvcResult(result, Dish.class), DISH_1));
    }

    @Test
    void getUnauth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + DISH_ID_1)
                .param("menuId", String.valueOf(LUNCH_MENU_ID_1)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + 1)
                .param("menuId", String.valueOf(LUNCH_MENU_ID_1))
                .with(userHttpBasic(USER_2)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL)
                .param("menuId", String.valueOf(LUNCH_MENU_ID_1))
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJsonDish(DISH_1, DISH_2));
    }
}
