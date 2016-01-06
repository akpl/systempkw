package pkw.models;

import org.joda.time.LocalDate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WynikiPytaniaReferendalneRepository extends CrudRepository<WynikiPytaniaReferendalne, Integer> {
    WynikiPytaniaReferendalne findOneByPytanieReferendalneAndKomisja(PytanieReferendalne pytanieReferendalne, Komisja komisja);
}
