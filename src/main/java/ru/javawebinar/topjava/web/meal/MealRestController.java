package ru.javawebinar.topjava.web.meal;

import static ru.javawebinar.topjava.AuthorizedUser.id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;
import static ru.javawebinar.topjava.util.ValidationUtil.checkIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {

    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);

    @Autowired
    private MealService service;

    public Meal update(Meal meal, int id) {
        log.info("Update meal {} for user {}", meal.getId(), AuthorizedUser.id());
        checkIdConsistent(meal, id);
        return service.update(meal, AuthorizedUser.id());
    }

    public Meal create(Meal meal) {
        log.info("Create meal for user {}", AuthorizedUser.id());
        checkNew(meal);
        return service.save(meal, AuthorizedUser.id());
    }

    public void delete(int id) {
        log.info("Delete meal {} for user {}", id, AuthorizedUser.id());
        service.delete(id, AuthorizedUser.id());
    }

    public Meal get(int id) {
        log.info("Get meal {} for user {}", id, AuthorizedUser.id());
        return service.get(id, AuthorizedUser.id());
    }

    public List<MealWithExceed> getAll(){
        log.info("GetAll for user {}", AuthorizedUser.id());
        return service.getAll(AuthorizedUser.id(), DEFAULT_CALORIES_PER_DAY);
    }

    public List<MealWithExceed> getBetween(LocalDate startDate, LocalDate endDate, LocalTime starTime, LocalTime endTime){
        log.info("GetBetween for user {}", AuthorizedUser.id());
        return service.getBetween(startDate, endDate, starTime, endTime, AuthorizedUser.id(), DEFAULT_CALORIES_PER_DAY);
    }

}