package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import java.util.List;

import static org.springframework.dao.support.DataAccessUtils.*;


@Repository
public class JdbcMealRepositoryImpl implements MealRepository {

    private static final RowMapper<Meal> rowMapper = new MealRowMapper();
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insert;

    @Autowired
    public JdbcMealRepositoryImpl(DataSource dataSource, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.insert = new SimpleJdbcInsert(dataSource).withTableName("meals").usingGeneratedKeyColumns("id");
    }

    @Override
    public Meal save(Meal meal, int userId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", meal.getId());
        map.addValue("datetime", meal.getDateTime());
        map.addValue("description", meal.getDescription());
        map.addValue("calories", meal.getCalories());
        map.addValue("user_id", userId);
        if (meal.isNew()){
            int id = insert.executeAndReturnKey(map).intValue();
            meal.setId(id);
        }else {
            if (namedParameterJdbcTemplate.update("UPDATE meals SET datetime=:datetime, description=:description, " +
                    "calories=:calories WHERE id=:id AND user_id=:user_id", map) == 0){
                return null;
            };
        }
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", id);
        map.addValue("user_id", userId);
        return namedParameterJdbcTemplate.update("DELETE FROM meals WHERE id=:id AND user_id=:user_id", map ) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return singleResult(jdbcTemplate.query("SELECT * FROM meals WHERE id=? AND user_id=?", rowMapper, id, userId ));    }

    @Override
    public List<Meal> getAll(int userId) {
        return jdbcTemplate.query("SELECT * FROM meals WHERE user_id=? ORDER BY datetime DESC", rowMapper, userId);
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("startDate", startDate);
        map.addValue("endDate", endDate);
        map.addValue("user_id", userId);
        return namedParameterJdbcTemplate
                .query("SELECT * FROM meals WHERE user_id=:user_id " +
                        "AND datetime>=:startDate AND datetime<=:endDate ORDER BY datetime DESC ", map, rowMapper);
    }

    private static final class MealRowMapper implements RowMapper<Meal>{

        @Override
        public Meal mapRow(ResultSet resultSet, int i) throws SQLException {
            Integer id = resultSet.getInt("id");
            LocalDateTime dateTime = LocalDateTime.parse(resultSet.getString("dateTime").substring(0, 16), DateTimeUtil.DATE_TIME_FORMATTER);
            String description = resultSet.getString("description");
            int calories = resultSet.getInt("calories");
            return new Meal(id, dateTime, description, calories);
        }
    }
}
