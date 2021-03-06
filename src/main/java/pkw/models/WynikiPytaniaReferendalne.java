package pkw.models;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

import javax.persistence.*;

@Entity
@Table(name = "wyniki_pytania_referendalne")
public class WynikiPytaniaReferendalne {
    private int id;
    private int odpowiedziTak;
    private int odpowiedziNie;
    private PytanieReferendalne pytanieReferendalne;
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
    @Column(name = "ODPOWIEDZI_TAK")
    public int getOdpowiedziTak() {
        return odpowiedziTak;
    }

    public void setOdpowiedziTak(int odpowiedziTak) {
        this.odpowiedziTak = odpowiedziTak;
    }

    @Basic
    @Column(name = "ODPOWIEDZI_NIE")
    public int getOdpowiedziNie() {
        return odpowiedziNie;
    }

    public void setOdpowiedziNie(int odpowiedziNie) {
        this.odpowiedziNie = odpowiedziNie;
    }

    @ManyToOne
    @JoinColumn(name = "PYTANIE_REFERENDALNE_ID", referencedColumnName = "ID", nullable = false)
    public PytanieReferendalne getPytanieReferendalne() {
        return pytanieReferendalne;
    }

    public void setPytanieReferendalne(PytanieReferendalne pytanieReferendalne) {
        this.pytanieReferendalne = pytanieReferendalne;
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

        WynikiPytaniaReferendalne that = (WynikiPytaniaReferendalne) o;

        if (odpowiedziTak != that.odpowiedziTak) return false;
        if (odpowiedziNie != that.odpowiedziNie) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = odpowiedziTak;
        result = 31 * result + odpowiedziNie;
        return result;
    }
}
