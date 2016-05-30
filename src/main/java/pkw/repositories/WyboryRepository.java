package pkw.repositories;

import org.joda.time.LocalDate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pkw.models.Wybory;

import java.util.List;

@Repository
public interface WyboryRepository extends CrudRepository<Wybory, Integer> {
    List<Wybory> findByOrderByIdAsc();
    @Query("FROM Wybory w WHERE w.dataGlosowania = ?")
    List<Wybory> dzisiejszeWybory(LocalDate today);

    List<Wybory> findByDataGlosowaniaBeforeOrderByIdAsc(LocalDate date);

    /**
     * Zwraca listę przyszłych wyborów posortowanych od najbliższych do najodleglejszych.
     * @return lista wyborów
     */
    @Query("FROM Wybory w WHERE w.dataGlosowania >= current_date AND (w.typWyborow.id = 1 OR w.typWyborow.id = 2) ORDER BY w.dataGlosowania ASC")
    List<Wybory> findNextPrezydenckieOrParlamentarne();

    @Query("FROM Wybory w WHERE w.typWyborow.id = 1 AND w.dataGlosowania < current_date ORDER BY w.dataGlosowania DESC")
    List<Wybory> findNextParlamentarne();

    @Query("FROM Wybory w WHERE w.typWyborow.id = 2 AND w.dataGlosowania < current_date ORDER BY w.dataGlosowania DESC")
    List<Wybory> findNextPrezydenckie();

    @Query("FROM Wybory w WHERE w.typWyborow.id = 3 AND w.dataGlosowania < current_date ORDER BY w.dataGlosowania DESC")
    List<Wybory> findNextReferendum();
}