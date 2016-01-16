package pkw.models;

import org.joda.time.LocalDate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WynikiParlamentarneRepository extends CrudRepository<WynikiParlamentarne, Integer> {
    WynikiParlamentarne findOneByWyboryAndKomitet(Wybory wybory, Komitet komitet);
    List<WynikiParlamentarne> findByWybory(Wybory wybory);
}
