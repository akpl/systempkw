package pkw.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pkw.models.KandydatPrezydent;
import pkw.models.Komisja;
import pkw.models.WynikiPrezydent;

@Repository
public interface WynikiPrezydentRepository extends CrudRepository<WynikiPrezydent, Integer> {
    WynikiPrezydent findOneByKandydatPrezydentAndKomisja(KandydatPrezydent kandydatPrezydent, Komisja komisja);
}
