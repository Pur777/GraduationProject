package ru.javawebinar.topjava.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.TestUtil;
import ru.javawebinar.topjava.model.LunchMenu;
import ru.javawebinar.topjava.service.LunchMenuService;
import ru.javawebinar.topjava.web.json.JsonUtil;
import ru.javawebinar.topjava.web.menu.AdminMenuRestController;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MenuAndDishTestData.assertMatch;
import static ru.javawebinar.topjava.MenuAndDishTestData.contentJson;
import static ru.javawebinar.topjava.MenuAndDishTestData.*;
import static ru.javawebinar.topjava.RestaurantTestData.*;
import static ru.javawebinar.topjava.TestUtil.readFromJson;
import static ru.javawebinar.topjava.TestUtil.userHttpBasic;
import static ru.javawebinar.topjava.UserTestData.ADMIN;
import static ru.javawebinar.topjava.UserTestData.USER_1;
import static ru.javawebinar.topjava.util.exception.ErrorType.VALIDATION_ERROR;
import static ru.javawebinar.topjava.web.ExceptionInfoHandler.EXCEPTION_DUPLICATE_RESTAURANT_AND_DATE;

public class AdminMenuRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = AdminMenuRestController.REST_URL + '/';

    @Autowired
    private LunchMenuService menuService;

    @Test
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + LUNCH_MENU_ID_1 + "?restaurantId=" + RESTAURANT_ID_1)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertMatch(TestUtil.readFromJsonMvcResult(result, LunchMenu.class), LUNCH_MENU_1));
    }

    @Test
    void getByDate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "by")
                .param("restaurantId", String.valueOf(RESTAURANT_ID_2))
                .param("date", LUNCH_MENU_2.getDate().toString())
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertMatch(TestUtil.readFromJsonMvcResult(result, LunchMenu.class), LUNCH_MENU_2));
    }

    @Test
    void getUnauth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + LUNCH_MENU_ID_3)
                .param("restaurantId", String.valueOf(RESTAURANT_ID_3)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + LUNCH_MENU_ID_3)
                .param("restaurantId", String.valueOf(RESTAURANT_ID_3))
                .with(userHttpBasic(USER_1)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + 1)
                .param("restaurantId", String.valueOf(RESTAURANT_ID_3))
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL)
                .param("restaurantId", String.valueOf(RESTAURANT_ID_1))
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(LUNCH_MENU_5, LUNCH_MENU_1));
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + LUNCH_MENU_ID_1)
                .param("restaurantId", String.valueOf(RESTAURANT_ID_1))
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isNoContent());
        assertMatch(menuService.getAll(RESTAURANT_ID_1), LUNCH_MENU_5);
    }

    @Test
    void deleteNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + 1)
                .param("restaurantId", String.valueOf(RESTAURANT_ID_1))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void update() throws Exception {
        LunchMenu updated = new LunchMenu(LUNCH_MENU_3);
        updated.setDate(LocalDate.of(2019, 9, 5));

        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + LUNCH_MENU_ID_3)
                .param("restaurantId", String.valueOf(RESTAURANT_ID_3))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isNoContent());

        assertMatch(menuService.get(RESTAURANT_ID_3, LUNCH_MENU_ID_3), updated);
    }

    @Test
    void createWithLocation() throws Exception {
        LunchMenu created = new LunchMenu(null, LocalDate.now());
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", String.valueOf(RESTAURANT_ID_3))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(created))
                .with(userHttpBasic(ADMIN)));

        LunchMenu returned = readFromJson(action, LunchMenu.class);
        created.setId(returned.getId());

        assertMatch(returned, created);
        assertMatch(menuService.getAll(RESTAURANT_ID_3), created, LUNCH_MENU_3);
    }

    @Test
    void createInvalid() throws Exception {
        LunchMenu invalid = new LunchMenu(null, null);
        mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", String.valueOf(RESTAURANT_ID_3))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR))
                .andDo(print());
    }

    @Test
    void updateInvalid() throws Exception {
        LunchMenu invalid = new LunchMenu(LUNCH_MENU_ID_1, null);
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + LUNCH_MENU_ID_1)
                .param("restaurantId", String.valueOf(RESTAURANT_ID_1))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR))
                .andDo(print());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicate() throws Exception {
        LunchMenu invalid = new LunchMenu(null, LUNCH_MENU_1.getDate());
        mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", String.valueOf(RESTAURANT_ID_1))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessage(EXCEPTION_DUPLICATE_RESTAURANT_AND_DATE));
    }
    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicate() throws Exception {
        LunchMenu invalid = new LunchMenu(LUNCH_MENU_ID_1, LUNCH_MENU_5.getDate());

        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + LUNCH_MENU_ID_1)
                .param("restaurantId", String.valueOf(RESTAURANT_ID_1))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessage(EXCEPTION_DUPLICATE_RESTAURANT_AND_DATE));
    }
}
