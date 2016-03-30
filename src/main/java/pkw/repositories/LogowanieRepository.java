package pkw.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pkw.models.Logowanie;

import java.util.List;

@Repository
public interface LogowanieRepository extends CrudRepository<Logowanie, Integer> {
    List<Logowanie> findByOrderByIdAsc();
    List<Logowanie> findByOrderByIdDesc();
}
