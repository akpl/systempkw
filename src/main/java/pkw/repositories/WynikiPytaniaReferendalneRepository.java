package pkw.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pkw.models.Komisja;
import pkw.models.PytanieReferendalne;
import pkw.models.WynikiPytaniaReferendalne;

@Repository
public interface WynikiPytaniaReferendalneRepository extends CrudRepository<WynikiPytaniaReferendalne, Integer> {
    WynikiPytaniaReferendalne findOneByPytanieReferendalneAndKomisja(PytanieReferendalne pytanieReferendalne, Komisja komisja);
}
