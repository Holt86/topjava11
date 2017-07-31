package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.javawebinar.topjava.util.DateTimeUtil.*;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {

    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, 1));
        save(new Meal(LocalDateTime.now(), "Моя еда", 900), 2);
        save(new Meal(LocalDateTime.now(), "Мой обед", 1200), 2);
    }


    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
        } else {
            if (get(meal.getId(), userId) == null) {
                return null;
            }
        }
        Map<Integer, Meal> mealsForUser = repository.getOrDefault(userId, new HashMap<>());
        mealsForUser.put(meal.getId(), meal);
        repository.put(userId, mealsForUser);
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        Map<Integer, Meal> meals = repository.get(userId);
        return meals != null && meals.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Map<Integer, Meal> meals = repository.get(userId);
        if (meals == null) {
            return null;
        }
        return meals.get(id);
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return getStream(userId).collect(Collectors.toList());
    }

    @Override
    public Collection<Meal> getBetween(LocalDate startDate, LocalDate endDate, int userId) {
        return getStream(userId).filter(meal -> isBetween(meal.getDate(), startDate, endDate)).collect(Collectors.toList());
    }

    public Stream<Meal> getStream(int userId) {
        Map<Integer, Meal> mealsForUser = repository.get(userId);
        return mealsForUser == null ? Stream.empty() : mealsForUser.values().stream()
                .sorted(Comparator.comparing(Meal::getDateTime, Comparator.reverseOrder()));


    }
}

