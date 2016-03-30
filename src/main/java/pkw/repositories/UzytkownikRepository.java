package pkw.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pkw.models.PoziomDostepu;
import pkw.models.Uzytkownik;

import java.util.List;

@Repository
public interface UzytkownikRepository extends CrudRepository<Uzytkownik, Integer> {
    List<Uzytkownik> findByOrderByIdAsc();
    List<Uzytkownik> findByLogin(String login);
    Uzytkownik findOneByLogin(String login);
    List<Uzytkownik> findByPoziomDostepu(PoziomDostepu poziomDostepu);
    @Query("SELECT u FROM Uzytkownik u LEFT JOIN u.komisja as komisja WHERE komisja is null and u.poziomDostepu.id = 2")
    List<Uzytkownik> dostepniPrzewodniczacyKomisji();
}
