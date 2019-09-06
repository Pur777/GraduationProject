package ru.javawebinar.topjava;

import org.springframework.test.web.servlet.ResultMatcher;
import ru.javawebinar.topjava.model.Restaurant;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.TestUtil.readListFromJsonMvcResult;

public class RestaurantTestData {
    public static final int RESTAURANT_ID_1 = 100003;
    public static final int RESTAURANT_ID_2 = 100004;
    public static final int RESTAURANT_ID_3 = 100005;
    public static final int RESTAURANT_ID_4 = 100006;


    public static final Restaurant RESTAURANT_1 = new Restaurant(RESTAURANT_ID_1, "Горыныч");
    public static final Restaurant RESTAURANT_2 = new Restaurant(RESTAURANT_ID_2, "Северяне");
    public static final Restaurant RESTAURANT_3 = new Restaurant(RESTAURANT_ID_3, "White Rabbit");
    public static final Restaurant RESTAURANT_4 = new Restaurant(RESTAURANT_ID_4, "Remy Kitchen Bakery");

    public static void assertMatch(Restaurant actual, Restaurant expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "menu");
    }

    public static void assertMatch(Iterable<Restaurant> actual, Restaurant... expected) {
        assertMatch(actual, List.of(expected));
    }

    public static void assertMatch(Iterable<Restaurant> actual, Iterable<Restaurant> expected) {
        assertThat(actual).isEqualTo(expected);
    }

    public static ResultMatcher contentJson(Restaurant... expected) {
        return contentJson(List.of(expected));
    }

    public static ResultMatcher contentJson(Iterable<Restaurant> expected) {
        return result -> assertThat(readListFromJsonMvcResult(result, Restaurant.class)).isEqualTo(expected);
    }
}
