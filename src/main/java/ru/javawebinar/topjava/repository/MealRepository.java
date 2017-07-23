package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;

import java.util.List;

/**
 * Created by user on 21.07.2017.
 */
public interface MealRepository {
    Meal get(int id);

    Meal save(Meal meal);

    Meal delete(int id);

    List<MealWithExceed> getAll();
}
