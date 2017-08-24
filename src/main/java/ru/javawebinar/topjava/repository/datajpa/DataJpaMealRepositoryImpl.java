package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepositoryImpl implements MealRepository {

    @Autowired
    private CrudMealRepository crudMealRepository;

    @Autowired
    private CrudUserRepository crudUserRepository;

    @Override
    public Meal save(Meal meal, int userId) {
        User user = crudUserRepository.getOne(userId);
        meal.setUser(user);
        if (!meal.isNew() && get(meal.getId(), userId) == null){
            return null;
        }
       return crudMealRepository.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return crudMealRepository.delete(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return crudMealRepository.findByIdAndUser_id(id, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudMealRepository.findByUser_idOrderByDateTimeDesc(userId);
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return crudMealRepository.findByUser_idAndDateTimeBetweenOrderByDateTimeDesc(userId, startDate, endDate);
    }

    @Override
    public Meal getMealWithUser(int id, int userId) {
        return crudMealRepository.getMealWithUser(id, userId);
    }
}
