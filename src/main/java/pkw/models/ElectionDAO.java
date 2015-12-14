package pkw.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;

@Repository
public class ElectionDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insert(Election election) {
        Object[] params = new Object[] { election.getVotingDate(), election.getElectionType().ordinal(), election.getCreatorId() };
        int[] types = new int[] { Types.DATE, Types.NUMERIC, Types.NUMERIC };

        jdbcTemplate.update("insert into wybory (DATA_UTWORZENIA, DATA_GLOSOWANIA, TYP_WYBOROW_ID, ID_TWORCY) "
                + "values (current_date, ?, ?, ?)",
                params, types);
    }
}
