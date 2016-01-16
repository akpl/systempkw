package pkw.models;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface KandydatPrezydentRepository extends CrudRepository<KandydatPrezydent, Integer> {
    @Query(value = "SELECT NVL(MAX(NR_NA_LISCIE) + 1, 1) FROM KANDYDACI_PREZYDENT WHERE WYBORY_ID = ?", nativeQuery = true)
    int getNextNrListy(int wyboryId);
    @Query(value = "SELECT (CASE WHEN COUNT(*) = 0 THEN 'false' ELSE 'true' END) FROM KANDYDACI_PREZYDENT WHERE WYBORY_ID = ? AND NR_NA_LISCIE = ?", nativeQuery = true)
    boolean kandydatPrezydentExists(int wyboryId, int nrListy);

    List<KandydatPrezydent> findByWybory(Wybory wybory);
}
