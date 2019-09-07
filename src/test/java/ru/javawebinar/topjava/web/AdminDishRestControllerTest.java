package ru.javawebinar.topjava.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.TestUtil;
import ru.javawebinar.topjava.model.Dish;
import ru.javawebinar.topjava.service.DishService;
import ru.javawebinar.topjava.web.dish.AdminDishRestController;
import ru.javawebinar.topjava.web.json.JsonUtil;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MenuAndDishTestData.*;
import static ru.javawebinar.topjava.TestUtil.readFromJson;
import static ru.javawebinar.topjava.TestUtil.userHttpBasic;
import static ru.javawebinar.topjava.UserTestData.ADMIN;
import static ru.javawebinar.topjava.UserTestData.USER_1;
import static ru.javawebinar.topjava.util.exception.ErrorType.VALIDATION_ERROR;

public class AdminDishRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = AdminDishRestController.REST_URL + '/';

    @Autowired
    private DishService dishService;

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + DISH_ID_2)
                .param("menuId", String.valueOf(LUNCH_MENU_ID_1))
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isNoContent());
        assertMatchDish(dishService.getAll(LUNCH_MENU_ID_1), DISH_1);
    }

    @Test
    void deleteUnauth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + DISH_ID_2)
                .param("menuId", String.valueOf(LUNCH_MENU_ID_1)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + DISH_ID_2)
                .param("menuId", String.valueOf(LUNCH_MENU_ID_1))
                .with(userHttpBasic(USER_1)))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + 1)
                .param("menuId", String.valueOf(LUNCH_MENU_ID_1))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void update() throws Exception {
        Dish updated = new Dish(DISH_3);
        updated.setName("Update");

        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + DISH_ID_3)
                .param("menuId", String.valueOf(LUNCH_MENU_ID_2))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isNoContent());

        assertMatchDish(dishService.get(LUNCH_MENU_ID_2, DISH_ID_3), updated);
    }

    @Test
    void createWithLocation() throws Exception {
        Dish created = new Dish(null, "Create", 9.99);
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .param("menuId", String.valueOf(LUNCH_MENU_ID_2))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(created))
                .with(userHttpBasic(ADMIN)));

        Dish returned = readFromJson(action, Dish.class);
        created.setId(returned.getId());

        assertMatchDish(returned, created);
        assertMatchDish(dishService.getAll(LUNCH_MENU_ID_2), DISH_3, DISH_4, created);
    }

    @Test
    void createInvalid() throws Exception {
        Dish invalid = new Dish(null, "  ", 9.99);
        mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .param("menuId", String.valueOf(LUNCH_MENU_ID_1))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR))
                .andDo(print());
    }
}
