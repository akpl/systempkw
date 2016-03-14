package pkw.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pkw.models.TypWyborow;

@Repository
public interface TypWyborowRepository extends CrudRepository<TypWyborow, Integer> {
}
