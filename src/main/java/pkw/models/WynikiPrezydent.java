package pkw.models;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

import javax.persistence.*;

@Entity
@Table(name = "WYNIKI_PREZYDENT")
public class WynikiPrezydent {
    private int id;
    private int liczbaGlosow;
    private KandydatPrezydent kandydatPrezydent;
    private Komisja komisja;
    private LocalDateTime czasWprowadzenia;

    @Id
    @GeneratedValue(generator="WynikiPrezydentId")
    @SequenceGenerator(name="WynikiPrezydentId",sequenceName="wyniki_prezydent_seq")
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
    @JoinColumn(name = "KANDYDAT_PREZYDENT_ID", referencedColumnName = "ID", nullable = false)
    public KandydatPrezydent getKandydatPrezydent() {
        return kandydatPrezydent;
    }

    public void setKandydatPrezydent(KandydatPrezydent kandydatPrezydent) {
        this.kandydatPrezydent = kandydatPrezydent;
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

        WynikiPrezydent that = (WynikiPrezydent) o;

        if (liczbaGlosow != that.liczbaGlosow) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return liczbaGlosow;
    }
}
