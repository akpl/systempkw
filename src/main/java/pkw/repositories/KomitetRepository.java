package pkw.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pkw.models.Komitet;
import pkw.models.Wybory;

import java.util.List;

@Repository
public interface KomitetRepository extends CrudRepository<Komitet, Integer> {
    @Query(value = "SELECT (CASE WHEN COUNT(*) = 0 THEN 'false' ELSE 'true' END) FROM komitety WHERE WYBORY_ID = ? AND NAZWA = ?", nativeQuery = true)
    boolean komitetExists(int wyboryId, String nazwa);

    List<Komitet> findByWybory(Wybory wybory);
    List<Komitet> findByWyboryOrderByNrAsc(Wybory wybory);
}
