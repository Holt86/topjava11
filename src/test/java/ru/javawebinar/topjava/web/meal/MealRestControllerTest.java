package ru.javawebinar.topjava.web.meal;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;


import java.time.LocalTime;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.util.MealsUtil.getFilteredWithExceeded;
import static ru.javawebinar.topjava.util.MealsUtil.getWithExceeded;

/**
 * Created by user on 08.09.2017.
 */
public class MealRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = MealRestController.REST_URL + '/';

    @Test
    public void testMeal() throws Exception {
        mockMvc.perform(get(REST_URL + MEAL2.getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentMatcher(MEAL2));
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + MEAL2.getId()))
                .andExpect(status().isOk())
                .andDo(print());
        MATCHER.assertListEquals(Arrays.asList(MEAL6, MEAL5, MEAL4, MEAL3, MEAL1), mealService.getAll(AuthorizedUser.id()));
    }

    @Test
    public void testUpdate() throws Exception {
        Meal updated = getUpdated();
        mockMvc.perform(put(REST_URL + updated.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isOk())
                .andDo(print());
        MATCHER.assertEquals(updated, mealService.get(updated.getId(), AuthorizedUser.id()));
    }


    @Test
    public void testCreate() throws Exception {
        Meal newMeal = getCreated();
        ResultActions actions = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMeal)))
                .andDo(print())
                .andExpect(status().isCreated());
        Meal returned = MATCHER.fromJsonAction(actions);
        newMeal.setId(returned.getId());
        MATCHER.assertEquals(newMeal, mealService.get(newMeal.getId(), AuthorizedUser.id()));
    }

    @Test
    public void testGetAll() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER_EXCEED.contentListMatcher(getWithExceeded(MEALS, AuthorizedUser.getCaloriesPerDay())));
    }

    @Test
    public void testGetFilter() throws Exception {
        mockMvc.perform(get(REST_URL + "filter")
                .param("start", "2015-05-30T12:15:30")
                .param("end", "2015-05-31T15:15:30"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER_EXCEED.contentListMatcher(
                        getFilteredWithExceeded(MEALS, LocalTime.of(12, 15, 30), LocalTime.of(15, 15, 30), AuthorizedUser.getCaloriesPerDay())));
    }

    @Test
    public void testGetBetween() throws Exception {
        mockMvc.perform(get(REST_URL + "between")
                .param("startDate", "2015-05-30")
                .param("startTime", "12:15:30")
                .param("endDate", "2015-05-31")
                .param("endTime", "15:15:30"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER_EXCEED.contentListMatcher(
                        getFilteredWithExceeded(MEALS, LocalTime.of(12, 15, 30), LocalTime.of(15, 15, 30), AuthorizedUser.getCaloriesPerDay())));
    }
}
