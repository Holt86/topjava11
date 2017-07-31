package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface MealService {

    Meal update(Meal meal, int userId);

    Meal save(Meal meal, int userId);

    Meal get(int id, int userId);

    void delete(int id, int userId);

    List<MealWithExceed> getAll(int userId, int calories);

    List<MealWithExceed> getBetween(LocalDate startDate, LocalDate endDate, LocalTime starTime, LocalTime endTime, int userId, int calories);
}