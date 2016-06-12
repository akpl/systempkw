package pkw.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FrekwencjaWyborczaOkregRepository extends CrudRepository<pkw.models.FrekwencjaWyborczaOkreg, Integer> {
}
