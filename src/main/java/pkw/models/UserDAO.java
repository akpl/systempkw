package pkw.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.integration.IntegrationAutoConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class UserDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insert(User user) {
        Object[] params = new Object[] { user.getLogin(), user.getPassword(), user.getName(), user.getSurname(), user.getRoleId() };
        int[] types = new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.NUMERIC };

        jdbcTemplate.update("insert into uzytkownicy (login, haslo, imie, nazwisko, poziom_dostepu_id) "
                        + "values (?, ?, ?, ?, ?)",
                params, types);
    }

    public List<User> selectAll() {
        List<User> users = new ArrayList<>();

        List<Map<String,Object>> rows = jdbcTemplate.queryForList("select u.*, p.id as role_id, p.nazwa as role from uzytkownicy u left join poziomy_dostepu p on u.poziom_dostepu_id = p.id");
        for (Map row : rows) {
            User user = new User();
            user.setId(((Number)row.get("id")).intValue());
            user.setLogin((String)row.get("login"));
            user.setName((String)row.get("imie"));
            user.setSurname((String)row.get("nazwisko"));
            user.setRoleId(((Number)row.get("role_id")).intValue());
            user.setRole((String)row.get("role"));
            users.add(user);
        }
        return users;
    }
}
