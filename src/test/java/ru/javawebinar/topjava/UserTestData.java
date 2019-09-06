package ru.javawebinar.topjava;

import org.springframework.test.web.servlet.ResultMatcher;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.TestUtil.readFromJsonMvcResult;
import static ru.javawebinar.topjava.TestUtil.readListFromJsonMvcResult;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class UserTestData {
    public static final int USER_1_ID = START_SEQ;
    public static final int USER_2_ID = START_SEQ + 1;
    public static final int ADMIN_ID = START_SEQ + 2;

    public static final User USER_1 = new User(USER_1_ID, "User", "user@mail.ru","password", Role.ROLE_USER);
    public static final User USER_2 = new User(USER_2_ID, "Second User", "seconduser@mail.ru","password", Role.ROLE_USER);
    public static final User ADMIN = new User(ADMIN_ID, "Admin", "admin@gmail.com","admin", Role.ROLE_ADMIN, Role.ROLE_USER);

    public static void assertMatch(User actual, User expected) {
        assertThat(actual).isEqualTo(expected);
    }

    public static void assertMatch(Iterable<User> actual, User... expected) {
        assertMatch(actual, List.of(expected));
    }

    public static void assertMatch(Iterable<User> actual, Iterable<User> expected) {
        assertThat(actual).isEqualTo(expected);
    }

    public static ResultMatcher contentJson(User... expected) {
        return result -> assertMatch(readListFromJsonMvcResult(result, User.class), List.of(expected));
    }

    public static ResultMatcher contentJson(User expected) {
        return result -> assertMatch(readFromJsonMvcResult(result, User.class), expected);
    }

    public static String jsonWithPassword(User user, String passw) {
        return JsonUtil.writeAdditionProps(user, "password", passw);
    }
}
