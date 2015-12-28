package pkw.models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WyboryRepository extends CrudRepository<Wybory, Integer> {
    List<Wybory> findByOrderByIdAsc();
}
