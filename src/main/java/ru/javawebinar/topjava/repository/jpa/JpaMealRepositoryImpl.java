package ru.javawebinar.topjava.repository.jpa;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

import static ru.javawebinar.topjava.model.Meal.*;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepositoryImpl implements MealRepository {

    @PersistenceContext
    private EntityManager en;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        meal.setUser(en.getReference(User.class, userId));
        if (meal.isNew()){
            en.persist(meal);
            return meal;
        }else {
            if (get(meal.getId(), userId) == null){
                return null;
            }
            return en.merge(meal);
//            return en.createNamedQuery(Meal.UPDATE_MEAL)
//                    .setParameter("id", meal.getId())
//                    .setParameter("dateTime", meal.getDateTime())
//                    .setParameter("description", meal.getDescription())
//                    .setParameter("calories", meal.getCalories())
//                    .setParameter("user_id", userId).executeUpdate() != 0 ? meal : null;
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return en.createNamedQuery(DELETE_MEAL).setParameter("id", id).setParameter("user_id", userId).executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return DataAccessUtils.singleResult(en.createNamedQuery(GET_MEAL, Meal.class)
                .setParameter("id", id).setParameter("user_id", userId).getResultList());
    }

    @Override
    public List<Meal> getAll(int userId) {
        return en.createNamedQuery(GET_ALL_MEALS, Meal.class).setParameter("user_id", userId).getResultList();
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return en.createNamedQuery(BETWEEN_MEAL, Meal.class).setParameter("user_id", userId)
                .setParameter("startDate", startDate).setParameter("endDate", endDate).getResultList();
    }
}