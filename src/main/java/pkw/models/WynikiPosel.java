package pkw.models;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

import javax.persistence.*;

@Entity
@Table(name = "wyniki_posel")
public class WynikiPosel {
    private int id;
    private int liczbaGlosow;
    private KandydatPosel kandydatPosel;
    private Komisja komisja;
    private LocalDateTime czasWprowadzenia;

    @Id
    @GeneratedValue
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "LICZBA_GLOSOW")
    public int getLiczbaGlosow() {
        return liczbaGlosow;
    }

    public void setLiczbaGlosow(int liczbaGlosow) {
        this.liczbaGlosow = liczbaGlosow;
    }

    @ManyToOne
    @JoinColumn(name = "KANDYDAT_POSEL_ID", referencedColumnName = "ID", nullable = false)
    public KandydatPosel getKandydatPosel() {
        return kandydatPosel;
    }

    public void setKandydatPosel(KandydatPosel kandydatPosel) {
        this.kandydatPosel = kandydatPosel;
    }

    @OneToOne
    @JoinColumn(name = "KOMISJA_NR", referencedColumnName = "NR", nullable = false)
    public Komisja getKomisja() {
        return komisja;
    }

    public void setKomisja(Komisja komisja) {
        this.komisja = komisja;
    }

    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    @Column(name = "CZAS_WPROWADZENIA")
    public LocalDateTime getCzasWprowadzenia() {
        return czasWprowadzenia;
    }

    public void setCzasWprowadzenia(LocalDateTime czasWprowadzenia) {
        this.czasWprowadzenia = czasWprowadzenia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WynikiPosel that = (WynikiPosel) o;

        if (liczbaGlosow != that.liczbaGlosow) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return liczbaGlosow;
    }
}
