package pkw.models;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Elimas on 2015-12-23.
 */
@Entity
@Table(name = "POZIOMY_DOSTEPU")
public class PoziomDostepu {
    private int id;
    private String nazwa;
    private Uzytkownik uzytkownik;
    private List<Uzytkownik> uzytkownicy;

    @Id
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "NAZWA")
    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PoziomDostepu that = (PoziomDostepu) o;

        if (id != that.id) return false;
        if (nazwa != null ? !nazwa.equals(that.nazwa) : that.nazwa != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (nazwa != null ? nazwa.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "poziomDostepu")
    public List<Uzytkownik> getUzytkownicy() {
        return uzytkownicy;
    }

    public void setUzytkownicy(List<Uzytkownik> uzytkownicy) {
        this.uzytkownicy = uzytkownicy;
    }
}
