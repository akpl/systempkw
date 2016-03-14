package pkw.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pkw.models.PoziomDostepu;

@Repository
public interface PoziomDostepuRepository extends CrudRepository<PoziomDostepu, Integer> {
}
