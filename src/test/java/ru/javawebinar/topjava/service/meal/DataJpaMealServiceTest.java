package ru.javawebinar.topjava.service.meal;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.Meal;

import static ru.javawebinar.topjava.MealTestData.MEAL1;
import static ru.javawebinar.topjava.MealTestData.MEAL1_ID;
import static ru.javawebinar.topjava.MealTestData.MEAL_MATCHER;
import static ru.javawebinar.topjava.UserTestData.USER;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.UserTestData.USER_MATCHER;

@ActiveProfiles(profiles = "datajpa")
public class DataJpaMealServiceTest extends AbstractMealServiceTest {
    @Test
    public void getMealWithUser() throws Exception {
        Meal mealWithUser = service.getMealWithUser(MEAL1_ID, USER_ID);
        MEAL_MATCHER.assertEquals(mealWithUser, MEAL1);
        USER_MATCHER.assertEquals(mealWithUser.getUser(), USER);

    }
}
