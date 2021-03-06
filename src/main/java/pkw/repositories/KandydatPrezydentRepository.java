package pkw.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pkw.models.KandydatPrezydent;
import pkw.models.Wybory;

import java.util.List;

@Repository
public interface KandydatPrezydentRepository extends CrudRepository<KandydatPrezydent, Integer> {
    @Query(value = "SELECT IFNULL(MAX(NR_NA_LISCIE) + 1, 1) FROM kandydaci_prezydent WHERE WYBORY_ID = ?", nativeQuery = true)
    int getNextNrListy(int wyboryId);
    @Query(value = "SELECT (CASE WHEN COUNT(*) = 0 THEN 'false' ELSE 'true' END) FROM kandydaci_prezydent WHERE WYBORY_ID = ? AND NR_NA_LISCIE = ?", nativeQuery = true)
    boolean kandydatPrezydentExists(int wyboryId, int nrListy);

    List<KandydatPrezydent> findByWybory(Wybory wybory);
}
