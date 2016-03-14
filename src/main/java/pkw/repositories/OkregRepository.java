package pkw.repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pkw.models.Okreg;

@Repository
public interface OkregRepository extends CrudRepository<Okreg, Integer> {
}
