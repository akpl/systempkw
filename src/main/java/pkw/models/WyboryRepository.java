package pkw.models;

import org.joda.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WyboryRepository extends CrudRepository<Wybory, Integer> {
    List<Wybory> findByOrderByIdAsc();
    @Query("FROM Wybory w WHERE w.dataGlosowania = ?")
    List<Wybory> dzisiejszeWybory(LocalDate today);

    List<Wybory> findByDataGlosowaniaBeforeOrderByIdAsc(LocalDate date);
}
