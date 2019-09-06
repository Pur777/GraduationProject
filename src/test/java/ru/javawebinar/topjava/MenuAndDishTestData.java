package ru.javawebinar.topjava;

import org.springframework.test.web.servlet.ResultMatcher;
import ru.javawebinar.topjava.model.Dish;
import ru.javawebinar.topjava.model.LunchMenu;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.TestUtil.readListFromJsonMvcResult;

public class MenuAndDishTestData {
    public static final int LUNCH_MENU_ID_1 = 100007;
    public static final int LUNCH_MENU_ID_2 = 100008;
    public static final int LUNCH_MENU_ID_3 = 100009;
    public static final int LUNCH_MENU_ID_4 = 100010;
    public static final int LUNCH_MENU_ID_5 = 100011;

    public static final int DISH_ID_1 = 100012;
    public static final int DISH_ID_2 = 100013;
    public static final int DISH_ID_3 = 100014;
    public static final int DISH_ID_4 = 100015;
    public static final int DISH_ID_5 = 100016;
    public static final int DISH_ID_6 = 100017;
    public static final int DISH_ID_7 = 100018;
    public static final int DISH_ID_8 = 100019;
    public static final int DISH_ID_9 = 100020;
    public static final int DISH_ID_10 = 100021;

    public static final Dish DISH_1 = new Dish(DISH_ID_1, "Утиная ножка конфи с морковью и мочеными яблоками", 760.0);
    public static final Dish DISH_2 = new Dish(DISH_ID_2, "Утиная грудка с пармантье из сельдерея и вишней", 680.0);
    public static final Dish DISH_3 = new Dish(DISH_ID_3, "Томленый говяжий язык в соусе из сладкой горчицы с малосольным огурцом", 760.0);
    public static final Dish DISH_4 = new Dish(DISH_ID_4, "Филе-миньон с лжекартофелем, белыми грибами и трюфельным соусом", 950.0);
    public static final Dish DISH_5 = new Dish(DISH_ID_5, "Жареный осьминог с картофелем и вешенками", 780.0);
    public static final Dish DISH_6 = new Dish(DISH_ID_6, "Подкопченный лосось с зеленым горошком и луковым муссом", 980.0);
    public static final Dish DISH_7 = new Dish(DISH_ID_7, "Сливочный суп с лососем", 480.0);
    public static final Dish DISH_8 = new Dish(DISH_ID_8, "Горячий борщ с ростбифом и салом", 350.0);
    public static final Dish DISH_9 = new Dish(DISH_ID_9, "Пирожок с картошкой", 50.0);
    public static final Dish DISH_10 = new Dish(DISH_ID_10, "Пирожок с капустой", 30.0);

    public static final LunchMenu LUNCH_MENU_1 = new LunchMenu(LUNCH_MENU_ID_1, LocalDate.of(2015, 5, 30), DISH_1, DISH_2);
    public static final LunchMenu LUNCH_MENU_2 = new LunchMenu(LUNCH_MENU_ID_2, LocalDate.of(2015, 5, 30), DISH_3, DISH_4);
    public static final LunchMenu LUNCH_MENU_3 = new LunchMenu(LUNCH_MENU_ID_3, LocalDate.of(2015, 5, 30), DISH_5, DISH_6);
    public static final LunchMenu LUNCH_MENU_4 = new LunchMenu(LUNCH_MENU_ID_4, LocalDate.of(2015, 5, 30), DISH_7, DISH_8);
    public static final LunchMenu LUNCH_MENU_5 = new LunchMenu(LUNCH_MENU_ID_5, LocalDate.of(2016, 2, 3), DISH_9, DISH_10);

    public static void assertMatch(LunchMenu actual, LunchMenu expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "restaurant", "dishes");
    }

    public static void assertMatch(Iterable<LunchMenu> actual, LunchMenu... expected) {
        assertMatch(actual, List.of(expected));
    }

    public static void assertMatch(Iterable<LunchMenu> actual, Iterable<LunchMenu> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("restaurant", "dishes").isEqualTo(expected);
    }

    public static void assertMatchDish(Dish actual, Dish expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "menu");
    }

    public static void assertMatchDish(Iterable<Dish> actual, Dish... expected) {
        assertMatchDish(actual, List.of(expected));
    }

    public static void assertMatchDish(Iterable<Dish> actual, Iterable<Dish> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("menu").isEqualTo(expected);
    }

    public static ResultMatcher contentJson(LunchMenu... expected) {
        return contentJson(List.of(expected));
    }

    public static ResultMatcher contentJson(Iterable<LunchMenu> expected) {
        return result -> assertThat(readListFromJsonMvcResult(result, LunchMenu.class)).isEqualTo(expected);
    }

    public static ResultMatcher contentJsonDish(Dish... expected) {
        return contentJsonDish(List.of(expected));
    }

    public static ResultMatcher contentJsonDish(Iterable<Dish> expected) {
        return result -> assertThat(readListFromJsonMvcResult(result, Dish.class)).isEqualTo(expected);
    }
}
