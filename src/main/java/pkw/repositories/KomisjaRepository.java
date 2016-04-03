package pkw.repositories;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pkw.models.Komisja;

@Repository
public interface KomisjaRepository extends CrudRepository<Komisja, Integer> {
    @Query(value = "SELECT sum(liczba_wyborcow) FROM komisje", nativeQuery = true)
    int getLiczbaWyborcow();
}
