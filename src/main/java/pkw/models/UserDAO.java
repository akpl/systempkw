package pkw.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public User getByLogin(String login) {
        if("admin".equals(login)) {
            return new User("admin", "admin", "Admin", "Admin");
        }
        return null;
    }
}
