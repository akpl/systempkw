package pkw.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pkw.models.*;

@Repository
public interface WynikiPoselRepository extends CrudRepository<WynikiPosel, Integer> {
    WynikiPosel findOneByKandydatPoselAndKomisja(KandydatPosel kandydatPosel, Komisja komisja);
    Iterable<WynikiPosel> findByKandydatPosel_Komitet_Wybory(Wybory wybory);
    Iterable<WynikiPosel> findByKomisja_OkregWyborczyAndKandydatPosel_Komitet_Wybory(Okreg okreg, Wybory wybory);
}
