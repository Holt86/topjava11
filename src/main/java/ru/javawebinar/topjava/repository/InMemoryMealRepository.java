package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import static ru.javawebinar.topjava.util.MealsUtil.getMeals;
import static ru.javawebinar.topjava.util.MealsUtil.getFilteredWithExceeded;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created by user on 21.07.2017.
 */
public class InMemoryMealRepository implements MealRepository {


    private static final Map<Integer, Meal> meals = new ConcurrentHashMap<>();
    private static final AtomicInteger count = new AtomicInteger(1);


    static {
        for (Meal meal : getMeals()){
            meal.setId(count.getAndIncrement());
            meals.put(meal.getId(), meal);
        }
    }


    @Override
    public Meal get(int id) {
        return meals.get(id);
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            meal.setId(count.getAndIncrement());
        }
        meals.put(meal.getId(), meal);
        return meal;
    }

    @Override
    public Meal delete(int id) {
        return meals.remove(id);
    }

    @Override
    public List<MealWithExceed> getAll() {
        return getFilteredWithExceeded(meals.values().stream().collect(Collectors.toList()), LocalTime.MIN, LocalTime.MAX, 2000);
    }
}
