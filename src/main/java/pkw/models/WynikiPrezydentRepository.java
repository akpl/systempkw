package pkw.models;

import org.springframework.data.repository.CrudRepository;

public interface WynikiPrezydentRepository extends CrudRepository<WynikiPrezydent, Integer> {
    WynikiPrezydent findOneByKandydatPrezydentAndKomisja(KandydatPrezydent kandydatPrezydent, Komisja komisja);
}
