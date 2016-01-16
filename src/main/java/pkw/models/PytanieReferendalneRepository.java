package pkw.models;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PytanieReferendalneRepository extends CrudRepository<PytanieReferendalne, Integer> {
    List<PytanieReferendalne> findByWybory(Wybory wybory);
}
