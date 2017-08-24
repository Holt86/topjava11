package ru.javawebinar.topjava.service.user;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;

import java.util.Arrays;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_MATCHER;

@ActiveProfiles(profiles = "datajpa")
public class DataJpaUserServiceTest extends AbstractUserServiceTest {

    @Test
    public void getUserWithMeals() throws Exception {
        User user = service.getUserWithMeals(UserTestData.ADMIN_ID);
        USER_MATCHER.assertEquals(user, UserTestData.ADMIN);
        MEAL_MATCHER.assertCollectionEquals(user.getMeals(), Arrays.asList(ADMIN_MEAL2, ADMIN_MEAL1));

    }
}
