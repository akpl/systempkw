package pkw.models;

import org.springframework.jdbc.core.JdbcTemplate;

public class UserDAO {
    private JdbcTemplate jdbcTemplate;
    public UserDAO(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }
    public User getByLogin(String login) {
        if("admin".equals(login)) {
            return new User("admin", "admin", "Admin", "Admin");
        }
        return null;
    }
}
