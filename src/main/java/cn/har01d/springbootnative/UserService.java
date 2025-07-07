package cn.har01d.springbootnative;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static final RowMapper<User> USER_ROW_MAPPER = (rs, rowNum) ->
            new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("age")
            );

    private final JdbcTemplate jdbcTemplate;

    public UserService(@Qualifier("sqlite3JdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                age INTEGER NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """);
    }

    public User save(User user) {
        if (user.getId() == 0) {
            jdbcTemplate.update("""
                INSERT INTO users(name, age) 
                VALUES (?, ?)
                """,
                    user.getName(), user.getAge());
            Integer id = jdbcTemplate.queryForObject(
                    "SELECT last_insert_rowid()",
                    Integer.class
            );
            user.setId(id);
        } else {
            jdbcTemplate.update("""
                UPDATE users SET name = ?, age = ? 
                WHERE id = ?
                """,
                    user.getName(), user.getAge(), user.getId());
        }
        return user;
    }

    public User findById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE id = ?", USER_ROW_MAPPER, id);
    }

    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM users ORDER BY id", USER_ROW_MAPPER);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM users WHERE id = ?", id);
    }
}
