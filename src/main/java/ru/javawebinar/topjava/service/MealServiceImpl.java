package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealWithExceed;


import static ru.javawebinar.topjava.util.MealsUtil.*;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class MealServiceImpl implements MealService {

    @Autowired
    private MealRepository repository;

    @Override
    public Meal update(Meal meal, int userId) {
        return checkNotFoundWithId(repository.save(meal, userId), meal.getId());
    }

    @Override
    public Meal save(Meal meal, int userId) {       ;
        return checkNotFoundWithId(repository.save(meal, userId), meal.getId());
    }

    @Override
    public Meal get(int id, int userId) {
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    @Override
    public void delete(int id, int userId) {
      checkNotFoundWithId(repository.delete(id, userId), userId);
    }

    @Override
    public List<MealWithExceed> getAll(int userId, int calories) {
        return getWithExceeded(repository.getAll(userId), calories);
    }

    @Override
    public List<MealWithExceed> getBetween(LocalDate startDate, LocalDate endDate, LocalTime starTime, LocalTime endTime, int userId, int calories) {
        return getFilteredWithExceeded(repository.getBetween(startDate, endDate, userId), starTime, endTime, calories);
    }
}