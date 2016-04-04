package pkw.models;

import javax.persistence.*;

@Entity
@Table(name = "komisje")
public class Komisja {
    @Id
    @GeneratedValue
    @Column(name = "NR")
    private int nr;

    @Column(name = "NAZWA")
    private String nazwa;

    @Column(name = "ADRES")
    private String adres;

    @Column(name = "LICZBA_WYBORCOW")
    private int liczbaWyborcow;

    @OneToOne()
    @JoinColumn(name = "ID_PRZEWODNICZACEGO", referencedColumnName = "ID", nullable = false)
    private Uzytkownik przewodniczacy;

    @ManyToOne
    @JoinColumn(name = "OKREG_WYBORCZY_NR", referencedColumnName = "NR", nullable = false)
    private Okreg okregWyborczy;

    @Transient
    private int przewodniczacyId;

    public int getNr() {
        return nr;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public int getLiczbaWyborcow() {
        return liczbaWyborcow;
    }

    public void setLiczbaWyborcow(int liczbaWyborcow) {
        this.liczbaWyborcow = liczbaWyborcow;
    }

    public Uzytkownik getPrzewodniczacy() {
        return przewodniczacy;
    }

    public void setPrzewodniczacy(Uzytkownik przewodniczacy) {
        this.przewodniczacy = przewodniczacy;
    }

    public Okreg getOkregWyborczy() {
        return okregWyborczy;
    }

    public void setOkregWyborczy(Okreg okregWyborczy) {
        this.okregWyborczy = okregWyborczy;
    }

    public int getPrzewodniczacyId() {
        return przewodniczacyId;
    }

    public void setPrzewodniczacyId(int przewodniczacyId) {
        this.przewodniczacyId = przewodniczacyId;
    }
}
