package pkw.models;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "okregi")
public class Okreg {
    @Id
    @GeneratedValue
    @Column(name = "NR")
    private int nr;

    @Column(name = "NAZWA")
    private String nazwa;

    @Column(name = "WOJEWODZTWO")
    private String wojewodztwo;

    @Column(name = "MIASTO")
    private String miasto;

    @Column(name = "LICZBA_MANDATOW")
    private int liczbaMandatow;

    @OneToMany(mappedBy = "okregWyborczy")
    private Set<Komisja> komisje;

    @OneToMany(mappedBy = "okreg")
    private Set<FrekwencjaWyborczaOkreg> frekwencjaWyborczaOkreg;

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

    public String getWojewodztwo() {
        return wojewodztwo;
    }

    public void setWojewodztwo(String wojewodztwo) {
        this.wojewodztwo = wojewodztwo;
    }

    public String getMiasto() {
        return miasto;
    }

    public void setMiasto(String miasto) {
        this.miasto = miasto;
    }

    public Set<Komisja> getKomisje() {
        return komisje;
    }

    public void setKomisje(Set<Komisja> komisje) {
        this.komisje = komisje;
    }

    public int getLiczbaMandatow() {
        return liczbaMandatow;
    }

    public void setLiczbaMandatow(int liczbaMandatow) {
        this.liczbaMandatow = liczbaMandatow;
    }

    public Set<FrekwencjaWyborczaOkreg> getFrekwencjaWyborczaOkreg() {
        return frekwencjaWyborczaOkreg;
    }

    public void setFrekwencjaWyborczaOkreg(Set<FrekwencjaWyborczaOkreg> frekwencjaWyborczaOkreg) {
        this.frekwencjaWyborczaOkreg = frekwencjaWyborczaOkreg;
    }
}