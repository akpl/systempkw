package pkw.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pkw.models.KandydatPosel;

@Repository
public interface KandydatPoselRepository extends CrudRepository<KandydatPosel, Integer> {
    @Query(value = "SELECT NVL(MAX(NR_NA_LISCIE) + 1, 1) FROM KANDYDACI_POSEL WHERE KOMITET_NR = ?", nativeQuery = true)
    int getNextNrListy(int komitetNr);
    @Query(value = "SELECT (CASE WHEN COUNT(*) = 0 THEN 'false' ELSE 'true' END) FROM KANDYDACI_POSEL WHERE KOMITET_NR = ? AND NR_NA_LISCIE = ?", nativeQuery = true)
    boolean kandydatPoselNrExists(int komitetNr, int nrListy);
}
