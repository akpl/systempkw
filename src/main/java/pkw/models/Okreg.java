package pkw.models;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "OKREGI")
public class Okreg {
    @Id
    @GeneratedValue(generator="OkregNr")
    @SequenceGenerator(name="OkregNr",sequenceName="okregi_seq")
    @Column(name = "NR")
    private int nr;

    @Column(name = "NAZWA")
    private String nazwa;

    @Column(name = "WOJEWODZTWO")
    private String wojewodztwo;

    @Column(name = "MIASTO")
    private String miasto;

    @OneToMany(mappedBy = "okregWyborczy")
    private Set<Komisja> komisje;

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
}