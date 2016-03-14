package pkw.repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pkw.models.Komisja;

@Repository
public interface KomisjaRepository extends CrudRepository<Komisja, Integer> {
}
