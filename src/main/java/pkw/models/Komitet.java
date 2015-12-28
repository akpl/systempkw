package pkw.models;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

/**
 * Created by Elimas on 2015-12-27.
 */
@Entity
@Table(name = "KOMITETY")
public class Komitet {
    private int nr;
    private String nazwa;

    @Id
    @GeneratedValue(generator="KomitetyId")
    @SequenceGenerator(name="KomitetyId",sequenceName="komitety_seq")
    @Column(name = "NR")
    public int getNr() {
        return nr;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }

    @NotBlank
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

        Komitet komitet = (Komitet) o;

        if (nr != komitet.nr) return false;
        if (nazwa != null ? !nazwa.equals(komitet.nazwa) : komitet.nazwa != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = nr;
        result = 31 * result + (nazwa != null ? nazwa.hashCode() : 0);
        return result;
    }
}
