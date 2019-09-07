package ru.javawebinar.topjava.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.TestUtil;
import ru.javawebinar.topjava.model.Restaurant;
import ru.javawebinar.topjava.service.RestaurantService;
import ru.javawebinar.topjava.web.json.JsonUtil;
import ru.javawebinar.topjava.web.restaurant.AdminRestaurantRestController;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.RestaurantTestData.*;
import static ru.javawebinar.topjava.TestUtil.readFromJson;
import static ru.javawebinar.topjava.TestUtil.userHttpBasic;
import static ru.javawebinar.topjava.UserTestData.ADMIN;
import static ru.javawebinar.topjava.UserTestData.USER_1;
import static ru.javawebinar.topjava.util.exception.ErrorType.VALIDATION_ERROR;
import static ru.javawebinar.topjava.web.ExceptionInfoHandler.EXCEPTION_DUPLICATE_NAME;


public class AdminRestaurantRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = AdminRestaurantRestController.REST_URL + '/';

    @Autowired
    private RestaurantService restaurantService;

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + RESTAURANT_ID_4)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isNoContent());
        assertMatch(restaurantService.getAll(), RESTAURANT_3, RESTAURANT_1, RESTAURANT_2);
    }

    @Test
    void deleteUnauth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + RESTAURANT_ID_4))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + RESTAURANT_ID_4)
                .with(userHttpBasic(USER_1)))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + 1)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void update() throws Exception {
        Restaurant updated = new Restaurant(RESTAURANT_2);
        updated.setName("Update");

        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT_ID_2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isNoContent());

        assertMatch(restaurantService.get(RESTAURANT_ID_2), updated);
    }

    @Test
    void createWithLocation() throws Exception {
        Restaurant created = new Restaurant(null, "Create");
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(created))
                .with(userHttpBasic(ADMIN)));

        Restaurant returned = readFromJson(action, Restaurant.class);
        created.setId(returned.getId());

        assertMatch(returned, created);
        assertMatch(restaurantService.getAll(), created, RESTAURANT_4, RESTAURANT_3, RESTAURANT_1, RESTAURANT_2);
    }

    @Test
    void createInvalid() throws Exception {
        Restaurant invalid = new Restaurant(null, "  ");
        mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
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
        Restaurant invalid = new Restaurant(RESTAURANT_ID_4, null);
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT_ID_4)
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
        Restaurant invalid = new Restaurant(null, RESTAURANT_3.getName());
        mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessage(EXCEPTION_DUPLICATE_NAME));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicate() throws Exception {
        Restaurant invalid = new Restaurant(RESTAURANT_ID_2, RESTAURANT_1.getName());

        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT_ID_2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessage(EXCEPTION_DUPLICATE_NAME));
    }

    @Test
    void updateHtmlUnsafe() throws Exception {
        Restaurant invalid = new Restaurant(RESTAURANT_ID_1, "<script>alert(123)</script>");
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT_ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR))
                .andDo(print());
    }
}
