package ru.javawebinar.topjava;

import ru.javawebinar.topjava.matcher.BeanMatcher;
import ru.javawebinar.topjava.model.BaseEntity;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Objects;

import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.model.BaseEntity.*;

public class MealTestData {

    public static final int MEAL_ID = START_SEQ + 2;

    public static final Meal MEAL_1 = new Meal(MEAL_ID, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500);
    public static final Meal MEAL_2 = new Meal(MEAL_ID + 1, LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000);
    public static final Meal MEAL_3 = new Meal(MEAL_ID + 2, LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500);
    public static final Meal MEAL_4 = new Meal(MEAL_ID + 3, LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 500);
    public static final Meal MEAL_5 = new Meal(MEAL_ID + 4, LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 1000);
    public static final Meal MEAL_6 = new Meal(MEAL_ID + 5, LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510);

    public static final Meal ADMIN_MEAL_1 = new Meal(MEAL_ID + 6, LocalDateTime.of(2015, Month.JUNE, 1, 14, 0), "Админ ланч", 510);
    public static final Meal ADMIN_MEAL_2 = new Meal(MEAL_ID + 7, LocalDateTime.of(2015, Month.JUNE, 1, 21, 0), "Админ ужин", 1500);
    public static final Meal ADMIN_MEAL_UPDATE_2 = new Meal(MEAL_ID + 7, LocalDateTime.of(2014, Month.JUNE, 1, 21, 0), "Новый Админ ужин", 1600);


    public static final BeanMatcher<Meal> MATCHER = new BeanMatcher<Meal>(
            (expected, actual) ->
                    expected == actual ||
                            (Objects.equals(expected.getId(), actual.getId())
                                    && Objects.equals(expected.getDateTime(), actual.getDateTime())
                                    && Objects.equals(expected.getDescription(), actual.getDescription())
                                    && expected.getCalories() == actual.getCalories()
                            )
    );

}
