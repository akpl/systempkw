package pkw.models;

import javax.persistence.*;

@Entity
@Table(name = "WYNIKI_PYTANIA_REFERENDALNE")
public class WynikiPytaniaReferendalne {
    private int id;
    private int odpowiedziTak;
    private int odpowiedziNie;
    private PytanieReferendalne pytanieReferendalne;
    private Komisja komisja;

    @Id
    @GeneratedValue(generator="WynikiPytaniaId")
    @SequenceGenerator(name="WynikiPytaniaId",sequenceName="wyniki_pytania_seq")
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
