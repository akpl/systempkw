package pkw.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pkw.models.Komitet;
import pkw.models.Wybory;
import pkw.models.WynikiParlamentarne;

import java.util.List;

@Repository
public interface WynikiParlamentarneRepository extends CrudRepository<WynikiParlamentarne, Integer> {
    WynikiParlamentarne findOneByWyboryAndKomitet(Wybory wybory, Komitet komitet);
    List<WynikiParlamentarne> findByWybory(Wybory wybory);
}
