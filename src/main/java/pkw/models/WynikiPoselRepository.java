package pkw.models;

import org.springframework.data.repository.CrudRepository;

public interface WynikiPoselRepository extends CrudRepository<WynikiPosel, Integer> {
    WynikiPosel findOneByKandydatPoselAndKomisja(KandydatPosel kandydatPosel, Komisja komisja);
}
