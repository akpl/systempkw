package pkw.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pkw.models.PytanieReferendalne;
import pkw.models.Wybory;

import java.util.List;

@Repository
public interface PytanieReferendalneRepository extends CrudRepository<PytanieReferendalne, Integer> {
    List<PytanieReferendalne> findByWybory(Wybory wybory);
}
