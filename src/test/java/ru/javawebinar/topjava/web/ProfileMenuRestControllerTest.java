package ru.javawebinar.topjava.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.TestUtil;
import ru.javawebinar.topjava.model.LunchMenu;
import ru.javawebinar.topjava.web.menu.ProfileMenuRestController;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MenuAndDishTestData.*;
import static ru.javawebinar.topjava.RestaurantTestData.RESTAURANT_ID_1;
import static ru.javawebinar.topjava.TestUtil.userHttpBasic;
import static ru.javawebinar.topjava.UserTestData.USER_1;

public class ProfileMenuRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = ProfileMenuRestController.REST_URL + '/';

    @Test
    void getLastMenu() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "last")
                .param("restaurantId", String.valueOf(RESTAURANT_ID_1))
                .param("date", LUNCH_MENU_2.getDate().toString())
                .with(userHttpBasic(USER_1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertMatch(TestUtil.readFromJsonMvcResult(result, LunchMenu.class), LUNCH_MENU_5));
    }

    @Test
    void getUnauth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "last")
                .param("restaurantId", String.valueOf(RESTAURANT_ID_1)))
                .andExpect(status().isUnauthorized());
    }
}
