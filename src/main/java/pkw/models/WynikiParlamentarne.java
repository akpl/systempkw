package pkw.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "wyniki_parlamentarne")
public class WynikiParlamentarne implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private int id;

    @Basic
    @Column(name = "LICZBA_POSLOW")
    private Integer liczbaPoslow;

    @OneToOne
    @JoinColumn(name = "WYBORY_ID", referencedColumnName = "ID", nullable = false)
    private Wybory wybory;

    @OneToOne
    @JoinColumn(name = "KOMITET_NR", referencedColumnName = "NR", nullable = false)
    private Komitet komitet;

    public Integer getLiczbaPoslow() {
        return liczbaPoslow;
    }

    public void setLiczbaPoslow(Integer liczbaPoslow) {
        this.liczbaPoslow = liczbaPoslow;
    }

    public Wybory getWybory() {
        return wybory;
    }

    public void setWybory(Wybory wybory) {
        this.wybory = wybory;
    }

    public Komitet getKomitet() {
        return komitet;
    }

    public void setKomitet(Komitet komitet) {
        this.komitet = komitet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WynikiParlamentarne)) return false;

        WynikiParlamentarne that = (WynikiParlamentarne) o;

        if (liczbaPoslow != null ? !liczbaPoslow.equals(that.liczbaPoslow) : that.liczbaPoslow != null) return false;
        if (wybory != null ? !wybory.equals(that.wybory) : that.wybory != null) return false;
        return komitet != null ? komitet.equals(that.komitet) : that.komitet == null;

    }

    @Override
    public int hashCode() {
        int result = liczbaPoslow != null ? liczbaPoslow.hashCode() : 0;
        result = 31 * result + (wybory != null ? wybory.hashCode() : 0);
        result = 31 * result + (komitet != null ? komitet.hashCode() : 0);
        return result;
    }
}
