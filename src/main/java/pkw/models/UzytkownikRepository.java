package pkw.models;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface UzytkownikRepository extends CrudRepository<Uzytkownik, Integer> {
    List<Uzytkownik> findByOrderByIdAsc();
    List<Uzytkownik> findByLogin(String login);
    List<Uzytkownik> findByPoziomDostepu(PoziomDostepu poziomDostepu);
    @Query("SELECT u FROM Uzytkownik u LEFT JOIN u.komisja as komisja WHERE komisja is null and u.poziomDostepu.id = 2")
    List<Uzytkownik> dostepniPrzewodniczacyKomisji();
}
