package pkw.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class ElectionDAO {
    @Autowired
    public JdbcTemplate jdbcTemplate;

    public void insert(Election election) {
        Object[] params = new Object[] { election.getVotingDate(), election.getElectionType(), election.getCreatorId() };
        int[] types = new int[] { Types.DATE, Types.NUMERIC, Types.NUMERIC };

        jdbcTemplate.update("insert into wybory (DATA_UTWORZENIA, DATA_GLOSOWANIA, TYP_WYBOROW_ID, ID_TWORCY) "
                + "values (current_date, ?, ?, ?)",
                params, types);
    }

    public List<Election> selectAll() {
        List<Election> elections = new ArrayList<Election>();

        List<Map<String,Object>> rows = jdbcTemplate.queryForList("select w.*, t.nazwa from wybory w join typy_wyborow t on t.id = w.typ_wyborow_id");
        for (Map row : rows) {
            Election election = new Election();
            election.setId(((Number)row.get("id")).intValue());
            election.setVotingDate((Date)row.get("data_glosowania"));
            election.setCreationDate((Date) row.get("data_utworzenia"));
            election.setElectionTypeName((String) row.get("nazwa"));
            elections.add(election);
        }
        return elections;
    }
}
