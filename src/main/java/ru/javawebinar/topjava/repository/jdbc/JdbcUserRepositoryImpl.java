package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private static final ResultSetExtractor<Collection<User>> EXTRACTOR = new UserResultSetExtractor();

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepositoryImpl(DataSource dataSource, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(dataSource)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        List<Map<String, Object>> batchValues = new ArrayList<>(user.getRoles().size());

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else {
            namedParameterJdbcTemplate.update(
                    "UPDATE users SET name=:name, email=:email, password=:password, " +
                            "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource);
        }
        deleteRole(user.getId());
        user.getRoles().stream()
                .forEach(role -> batchValues.add(new MapSqlParameterSource("user_id", user.getId()).addValue("role", role.toString()).getValues()));

        namedParameterJdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) VALUES (:user_id, :role)", batchValues.toArray(new Map[user.getRoles().size()]));

        return user;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public boolean delete(int id) {
        deleteRole(id);
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

//    @Override
//    public User get(int id) {
//        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
//        return DataAccessUtils.singleResult(users);
//    }

    @Override
    public User get(int id) {
        return DataAccessUtils.singleResult(jdbcTemplate.query("SELECT * FROM users INNER JOIN user_roles ON id = user_roles.user_id WHERE id=?", EXTRACTOR, id));
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        return DataAccessUtils.singleResult(jdbcTemplate.query("SELECT * FROM users INNER JOIN user_roles ON id = user_roles.user_id WHERE email=?", EXTRACTOR, email));
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users INNER JOIN user_roles ON id = user_roles.user_id ORDER BY name, email", EXTRACTOR)
                .stream().collect(Collectors.toList());
    }


    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public void deleteRole(int user_id) {
        jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user_id);
    }

    public static class UserResultSetExtractor implements ResultSetExtractor<Collection<User>> {
        @Override
        public Collection<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Integer, User> users = new LinkedHashMap<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                Role role = Role.valueOf(rs.getString("role"));
                if (users.computeIfPresent(id, (k, v) -> {
                            v.getRoles().add(role);
                            return v;
                        }
                ) != null) {
                    continue;
                } else {
                    users.put(id, new User(id, rs.getString("name"), rs.getString("email"), rs.getString("password"),
                            rs.getInt("calories_per_day"), rs.getBoolean("enabled"), EnumSet.of(role)));
                }
            }
            return users.values();
        }
    }
}
