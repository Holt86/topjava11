package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000).stream().forEach(System.out::println);
//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed>  getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> calories = mealList.stream()
                .collect(Collectors.toMap(p -> p.getDateTime().toLocalDate(), p -> p.getCalories(), (name1, name2) -> name1 + name2));

        return mealList.stream().filter(meal -> betweenDate(startTime, endTime, meal.getDateTime().toLocalTime()))
                .map(meal -> new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), calories.get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExceed>  getFilteredWithExceededByCycle(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExceed> mealWithExceeds = new ArrayList<>();
        Map<LocalDate, Integer> calories = new HashMap<>();
        for (UserMeal meal : mealList){
            if (calories.containsKey(meal.getDateTime().toLocalDate())){
                calories.put(meal.getDateTime().toLocalDate(), calories.get(meal.getDateTime().toLocalDate()) + meal.getCalories());
            }else {
                calories.put(meal.getDateTime().toLocalDate(), meal.getCalories());
            }
        }
        for (UserMeal meal : mealList){
            if (betweenDate(startTime, endTime, meal.getDateTime().toLocalTime())){
                mealWithExceeds.add(new UserMealWithExceed(meal.getDateTime(), meal.getDescription(),
                        meal.getCalories(), calories.get(meal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        }
        return mealWithExceeds;
    }

    public static boolean betweenDate(LocalTime startTime, LocalTime endTime, LocalTime time){
        return time.isAfter(startTime) && time.isBefore(endTime);
    }
}
