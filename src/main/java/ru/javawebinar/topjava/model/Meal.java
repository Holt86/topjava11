package ru.javawebinar.topjava.model;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@NamedQueries({
        @NamedQuery(name = Meal.GET_ALL_MEALS, query = "SELECT m FROM Meal m WHERE m.user.id =:user_id ORDER BY m.dateTime DESC "),
        @NamedQuery(name = Meal.GET_MEAL, query = "SELECT m FROM Meal m WHERE m.id=:id AND m.user.id=:user_id"),
        @NamedQuery(name = Meal.DELETE_MEAL, query = "DELETE from Meal m WHERE m.id=:id AND m.user.id=:user_id"),
        @NamedQuery(name = Meal.BETWEEN_MEAL, query = "SELECT m FROM Meal m WHERE m.user.id=:user_id " +
                "AND m.dateTime>=:startDate AND m.dateTime<=:endDate ORDER BY m.dateTime DESC"),
        @NamedQuery(name = Meal.UPDATE_MEAL, query = "UPDATE Meal m SET m.dateTime=:dateTime, m.description=:description, " +
                "m.calories=:calories WHERE m.id=:id AND m.user.id=:user_id")
})
@Entity
@Table(name = "meals", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "date_time"}, name = "meals_unique_user_datetime_idx")})
public class Meal extends BaseEntity {

    public static final String GET_ALL_MEALS = "Meal.getAll";
    public static final String GET_MEAL = "Meal.get";
    public static final String DELETE_MEAL = "Meal.delete";
    public static final String BETWEEN_MEAL = "Meal.between";
    public static final String UPDATE_MEAL = "Meal.update";

    @NotNull
    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @NotBlank
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "calories")
    @Range(min = 10, max = 10000)
    private int calories;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Meal() {
    }

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this(null, dateTime, description, calories);
    }

    public Meal(Integer id, LocalDateTime dateTime, String description, int calories) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}
