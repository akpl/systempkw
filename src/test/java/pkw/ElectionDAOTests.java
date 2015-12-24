package pkw;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;


public class ElectionDAOTests {
    //TODO testy do przepisania na repository
    /*ElectionDAO electionDAO;

    @Before
    public void setup() throws Exception {
        electionDAO = new ElectionDAO();
        electionDAO.jdbcTemplate = mock(JdbcTemplate.class);
    }

    @Test
    public void selectAllElectionsWithEmptyTable() {
        when(electionDAO.jdbcTemplate.queryForList(contains("from wybory"))).thenReturn(new ArrayList<>());

        List<Election> elections = electionDAO.selectAll();

        Assert.assertEquals(0, elections.size());
    }

    @Test
    public void selectAllElections() throws Exception {
        List<Map<String,Object>> sampleElections = new ArrayList<>();
        Map<String,Object> sampleElection = new HashMap<>();
        sampleElection.put("id", 123);
        Date sampleVotingDate = new SimpleDateFormat("dd/MM/yyyy").parse("13/14/2015");
        sampleElection.put("data_glosowania", sampleVotingDate);
        Date sampleCreationDate = new SimpleDateFormat("dd/MM/yyyy").parse("07/07/2017");
        sampleElection.put("data_utworzenia", sampleCreationDate);
        sampleElection.put("nazwa", "PARLAMENTARNE");
        sampleElections.add(sampleElection);
        when(electionDAO.jdbcTemplate.queryForList(contains("from wybory"))).thenReturn(sampleElections);

        List<Election> elections = electionDAO.selectAll();

        Assert.assertEquals(1, elections.size());
        Assert.assertEquals(123, elections.get(0).getId());
        Assert.assertEquals(new SimpleDateFormat("dd/MM/yyyy").parse("13/14/2015"), elections.get(0).getVotingDate());
        Assert.assertEquals(new SimpleDateFormat("dd/MM/yyyy").parse("07/07/2017"), elections.get(0).getCreationDate());
        Assert.assertEquals("PARLAMENTARNE", elections.get(0).getElectionTypeName());
    }*/
}
