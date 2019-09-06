package ru.javawebinar.topjava;

import org.springframework.test.web.servlet.ResultMatcher;
import ru.javawebinar.topjava.model.Vote;
import ru.javawebinar.topjava.to.VoteTo;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.TestUtil.readListFromJsonMvcResult;
import static ru.javawebinar.topjava.UserTestData.*;

public class VoteTestData {

    public static final int VOTE_ID_1 = 100022;
    public static final int VOTE_ID_2 = 100023;
    public static final int VOTE_ID_3 = 100024;
    public static final int VOTE_ID_4 = 100025;
    public static final int VOTE_ID_5 = 100026;
    public static final int VOTE_ID_6 = 100027;
    public static final int VOTE_ID_7 = 100028;
    public static final int VOTE_ID_8 = 100029;
    public static final int VOTE_ID_9 = 100030;
    public static final int VOTE_ID_10 = 100031;

    public static final Vote VOTE_1 = new Vote(VOTE_ID_1, LocalDate.of(2019,7,19), USER_1_ID, "Горыныч");
    public static final Vote VOTE_2 = new Vote(VOTE_ID_2, LocalDate.of(2019,7,19), USER_2_ID, "Горыныч");
    public static final Vote VOTE_3 = new Vote(VOTE_ID_3, LocalDate.of(2019,7,19), ADMIN_ID, "White Rabbit");
    public static final Vote VOTE_4 = new Vote(VOTE_ID_4, LocalDate.of(2019,7,20), USER_1_ID, "Remy Kitchen Bakery");
    public static final Vote VOTE_5 = new Vote(VOTE_ID_5, LocalDate.of(2019,7,20), USER_2_ID, "Remy Kitchen Bakery");
    public static final Vote VOTE_6 = new Vote(VOTE_ID_6, LocalDate.of(2019,7,20), ADMIN_ID, "Remy Kitchen Bakery");
    public static final Vote VOTE_7 = new Vote(VOTE_ID_7, LocalDate.of(2019,7,21), USER_1_ID, "Горыныч");
    public static final Vote VOTE_8 = new Vote(VOTE_ID_8, LocalDate.of(2019,7,21), USER_2_ID, "Северяне");
    public static final Vote VOTE_9 = new Vote(VOTE_ID_9, LocalDate.of(2019,7,21), ADMIN_ID, "White Rabbit");
    public static final Vote VOTE_10 = new Vote(VOTE_ID_10, LocalDate.of(2019,7,22), USER_2_ID, "Горыныч");

    public static final List<VoteTo> RATING_GROUP_BY_DATE = List.of(new VoteTo(LocalDate.of(2019, 7, 22), 1),
            new VoteTo(LocalDate.of(2019, 7, 21), 1), new VoteTo(LocalDate.of(2019, 7, 19), 2));

    public static void assertMatch(Vote actual, Vote expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected);
    }

    public static void assertMatch(Iterable<Vote> actual, Vote... expected) {
        assertMatch(actual, List.of(expected));
    }

    public static void assertMatch(Iterable<Vote> actual, Iterable<Vote> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields().isEqualTo(expected);
    }

    public static void assertMatchRating(int actual, int expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected);
    }

    public static ResultMatcher contentJson(Vote... expected) {
        return contentJson(List.of(expected));
    }

    public static ResultMatcher contentJson(Iterable<Vote> expected) {
        return result -> assertThat(readListFromJsonMvcResult(result, Vote.class)).isEqualTo(expected);
    }
    public static ResultMatcher contentJsonTo(Iterable<VoteTo> expected) {
        return result -> assertThat(readListFromJsonMvcResult(result, VoteTo.class)).usingElementComparatorIgnoringFields().isEqualTo(expected);
    }
}
