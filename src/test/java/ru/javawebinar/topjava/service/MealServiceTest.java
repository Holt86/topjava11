package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DbPopulator;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

/**
 * Created by user on 04.08.2017.
 */
@ContextConfiguration({"classpath:spring/spring-app.xml",
                       "classpath:spring/spring-db.xml"})
@RunWith(SpringRunner.class)
public class MealServiceTest {

    @Autowired
    private MealService service;

    @Autowired
    private DbPopulator populator;

    static {
        SLF4JBridgeHandler.install();
    }

    @Before
    public void setUp(){
        populator.execute();
    }

    @Test
    public void get() throws Exception {
       MealTestData.MATCHER.assertEquals(MEAL_4, service.get(MEAL_ID + 3, USER_ID));
    }

    @Test(expected = NotFoundException.class)
    public void testNotFoundGet() throws Exception{
        service.get(100004, 100001);
    }

    @Test
    public void delete() throws Exception {
        service.delete(MEAL_ID, USER_ID);
        MealTestData.MATCHER.assertCollectionEquals(Arrays.asList(MEAL_6, MEAL_5, MEAL_4, MEAL_3, MEAL_2), service.getAll(USER_ID));
    }

    @Test(expected = NotFoundException.class)
    public void testNotFoundDelete() throws Exception{
        service.delete(100003, 100001);
    }

    @Test
    public void getBetweenDateTimes() throws Exception {
       MealTestData.MATCHER.assertCollectionEquals(Arrays.asList(MEAL_4, MEAL_3, MEAL_2, MEAL_1),
               service.getBetweenDateTimes(LocalDateTime.of(2015, Month.MAY, 30, 9, 0), LocalDateTime.of(2015, Month.MAY, 31, 12, 0), USER_ID));
    }

    @Test
    public void getAll() throws Exception {
      MealTestData.MATCHER.assertCollectionEquals(Arrays.asList(ADMIN_MEAL_2, ADMIN_MEAL_1), service.getAll(ADMIN_ID));
    }

    @Test
    public void update() throws Exception {
      service.update(ADMIN_MEAL_UPDATE_2, ADMIN_ID);
      MealTestData.MATCHER.assertEquals(ADMIN_MEAL_UPDATE_2, service.get(ADMIN_MEAL_UPDATE_2.getId(), ADMIN_ID));
    }

    @Test(expected = NotFoundException.class)
    public void testNotFoundUpdate() throws Exception{
        service.update(ADMIN_MEAL_UPDATE_2, USER_ID);
    }

    @Test
    public void save() throws Exception {
        Meal newMeal = new Meal(LocalDateTime.of(2017, 8, 05, 10, 10), "Testing meal", 400);
        int id = service.save(newMeal, ADMIN_ID).getId();
        newMeal.setId(id);
        MealTestData.MATCHER.assertEquals(newMeal, service.get(newMeal.getId(), ADMIN_ID));
    }

    @Test(expected = DataAccessException.class)
    public void testDuplicateMailSave() throws Exception{
        service.save(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Дубликат Обед", 1000), USER_ID);
    }

}