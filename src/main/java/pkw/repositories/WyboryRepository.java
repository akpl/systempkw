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
}
