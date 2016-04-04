package pkw.models;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Elimas on 2015-12-27.
 */
@Entity
@Table(name = "pytania_referendalne")
public class PytanieReferendalne {
    private int id;
    private String pytanie;
    private Wybory wybory;
    private List<WynikiPytaniaReferendalne> wyniki;

    @Id
    @GeneratedValue
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NotBlank
    @Column(name = "PYTANIE")
    public String getPytanie() {
        return pytanie;
    }

    public void setPytanie(String pytanie) {
        this.pytanie = pytanie;
    }

    @ManyToOne
    @JoinColumn(name = "WYBORY_ID", referencedColumnName = "ID", nullable = false)
    public Wybory getWybory() {
        return wybory;
    }

    public void setWybory(Wybory wybory) {
        this.wybory = wybory;
    }

    @OneToMany(mappedBy = "pytanieReferendalne")
    public List<WynikiPytaniaReferendalne> getWyniki() {
        return wyniki;
    }

    public void setWyniki(List<WynikiPytaniaReferendalne> wyniki) {
        this.wyniki = wyniki;
    }

    public WynikiPytaniaReferendalne getWynikiDlaKomisji(Komisja komisja) {
        for (WynikiPytaniaReferendalne wyniki : getWyniki()) {
            if (wyniki.getKomisja() == komisja) {
                return wyniki;
            }
        }
        return null;
    }

    @Transient
    public WynikiPytaniaReferendalne getWynikLaczny() {
        WynikiPytaniaReferendalne wynikLaczny = new WynikiPytaniaReferendalne();
        int odpowiedziTak = 0;
        int odpowiedziNie = 0;
        for (WynikiPytaniaReferendalne wyniki : getWyniki()) {
            odpowiedziTak += wyniki.getOdpowiedziTak();
            odpowiedziNie += wyniki.getOdpowiedziNie();
        }
        wynikLaczny.setPytanieReferendalne(this);
        wynikLaczny.setOdpowiedziTak(odpowiedziTak);
        wynikLaczny.setOdpowiedziNie(odpowiedziNie);

        return wynikLaczny;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PytanieReferendalne that = (PytanieReferendalne) o;

        if (id != that.id) return false;
        if (pytanie != null ? !pytanie.equals(that.pytanie) : that.pytanie != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (pytanie != null ? pytanie.hashCode() : 0);
        return result;
    }
}
