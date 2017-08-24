package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    @Modifying
    @Transactional
    @Override
    Meal save(Meal meal);

    @Modifying
    @Transactional
    @Query("UPDATE Meal m SET m.dateTime=:dateTime, m.description=:description, m.calories=:calories WHERE m.id=:id AND m.user.id=:user_id")
    int update(@Param("id") int id, @Param("user_id") int userId,
               @Param("dateTime") LocalDateTime dateTime, @Param("description") String description, @Param("calories") int calories);

    @Modifying
    @Transactional
    @Query("DELETE FROM Meal m WHERE m.id=:id AND m.user.id=:user_id")
    int delete (@Param("id") int id, @Param("user_id") int userId);

    Meal findByIdAndUser_id(int id, int user_id);

    List<Meal> findByUser_idOrderByDateTimeDesc(int user_id);

    List<Meal> findByUser_idAndDateTimeBetweenOrderByDateTimeDesc(int user_id, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT m FROM Meal m LEFT JOIN FETCH m.user WHERE m.id=:id AND m.user.id=:userId")
    Meal getMealWithUser(@Param("id") int id, @Param("userId") int userId);


}
