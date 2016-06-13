package pkw.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pkw.models.FrekwencjaWyborczaOkreg;
import pkw.models.Wybory;

@Repository
public interface FrekwencjaWyborczaOkregRepository extends CrudRepository<pkw.models.FrekwencjaWyborczaOkreg, Integer> {
    Iterable<FrekwencjaWyborczaOkreg> findByWybory(Wybory wybory);
}
