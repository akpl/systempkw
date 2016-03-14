package pkw.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pkw.models.KandydatPosel;
import pkw.models.Komisja;
import pkw.models.WynikiPosel;

@Repository
public interface WynikiPoselRepository extends CrudRepository<WynikiPosel, Integer> {
    WynikiPosel findOneByKandydatPoselAndKomisja(KandydatPosel kandydatPosel, Komisja komisja);
}
