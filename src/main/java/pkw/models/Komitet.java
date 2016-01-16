package pkw.models;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Elimas on 2015-12-27.
 */
@Entity
@Table(name = "KOMITETY")
public class Komitet {
    private int nr;
    private String nazwa;
    private Wybory wybory;
    private List<KandydatPosel> kandydaciPosel;
    private List<WynikiParlamentarne> wyniki;

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

    @ManyToOne
    @JoinColumn(name = "WYBORY_ID", referencedColumnName = "ID", nullable = false)
    public Wybory getWybory() {
        return wybory;
    }

    public void setWybory(Wybory wybory) {
        this.wybory = wybory;
    }

    @org.hibernate.annotations.OrderBy(clause = "NR_NA_LISCIE ASC")
    @OneToMany(mappedBy = "komitet")
    public List<KandydatPosel> getKandydaciPosel() {
        return kandydaciPosel;
    }

    public void setKandydaciPosel(List<KandydatPosel> kandydaciPosel) {
        this.kandydaciPosel = kandydaciPosel;
    }

    @OneToMany(mappedBy = "komitet")
    public List<WynikiParlamentarne> getWyniki() {
        return wyniki;
    }

    public void setWyniki(List<WynikiParlamentarne> wyniki) {
        this.wyniki = wyniki;
    }

    @Transient
    public WynikiParlamentarne getWynikLaczny() {
        int liczbaPoslow = 0;
        for (WynikiParlamentarne wyniki : getWyniki()) {
            liczbaPoslow += wyniki.getLiczbaPoslow();
        }

        WynikiParlamentarne wynikLaczny = new WynikiParlamentarne();
        wynikLaczny.setKomitet(this);
        wynikLaczny.setLiczbaPoslow(liczbaPoslow);
        wynikLaczny.setWybory(getWybory());

        return wynikLaczny;
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
