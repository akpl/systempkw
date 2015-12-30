package pkw.models;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface KomitetRepository extends CrudRepository<Komitet, Integer> {
    @Query(value = "SELECT (CASE WHEN COUNT(*) = 0 THEN 'false' ELSE 'true' END) FROM KOMITETY WHERE WYBORY_ID = ? AND NAZWA = ?", nativeQuery = true)
    boolean komitetExists(int wyboryId, String nazwa);
}
