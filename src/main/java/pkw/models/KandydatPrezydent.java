package pkw.models;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Created by Elimas on 2015-12-27.
 */
@Entity
@Table(name = "KANDYDACI_PREZYDENT")
public class KandydatPrezydent {
    private int id;
    private String imie;
    private String nazwisko;
    private String plec;
    private String zawod;
    private String miejsceZamieszkania;
    private int nrNaLiscie;
    private String partia;
    private Wybory wybory;

    @Id
    @GeneratedValue(generator="KandydaciPrezydentId")
    @SequenceGenerator(name="KandydaciPrezydentId",sequenceName="kandydaci_prezydent_seq")
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NotBlank
    @Column(name = "IMIE")
    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    @NotBlank
    @Column(name = "NAZWISKO")
    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    @NotBlank
    @Pattern(regexp="M|K")
    @Size(min = 1, max = 1)
    @Column(name = "PLEC")
    public String getPlec() {
        return plec;
    }

    public void setPlec(String plec) {
        this.plec = plec;
    }

    @NotBlank
    @Column(name = "ZAWOD")
    public String getZawod() {
        return zawod;
    }

    public void setZawod(String zawod) {
        this.zawod = zawod;
    }

    @NotBlank
    @Column(name = "MIEJSCE_ZAMIESZKANIA")
    public String getMiejsceZamieszkania() {
        return miejsceZamieszkania;
    }

    public void setMiejsceZamieszkania(String miejsceZamieszkania) {
        this.miejsceZamieszkania = miejsceZamieszkania;
    }

    @NotNull
    @Column(name = "NR_NA_LISCIE")
    public int getNrNaLiscie() {
        return nrNaLiscie;
    }

    public void setNrNaLiscie(int nrNaLiscie) {
        this.nrNaLiscie = nrNaLiscie;
    }

    @NotBlank
    @Column(name = "PARTIA")
    public String getPartia() {
        return partia;
    }

    public void setPartia(String partia) {
        this.partia = partia;
    }

    @ManyToOne
    @JoinColumn(name = "WYBORY_ID", referencedColumnName = "ID", nullable = false)
    public Wybory getWybory() {
        return wybory;
    }

    public void setWybory(Wybory wybory) {
        this.wybory = wybory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KandydatPrezydent that = (KandydatPrezydent) o;

        if (id != that.id) return false;
        if (plec != that.plec) return false;
        if (nrNaLiscie != that.nrNaLiscie) return false;
        if (imie != null ? !imie.equals(that.imie) : that.imie != null) return false;
        if (nazwisko != null ? !nazwisko.equals(that.nazwisko) : that.nazwisko != null) return false;
        if (zawod != null ? !zawod.equals(that.zawod) : that.zawod != null) return false;
        if (miejsceZamieszkania != null ? !miejsceZamieszkania.equals(that.miejsceZamieszkania) : that.miejsceZamieszkania != null)
            return false;
        if (partia != null ? !partia.equals(that.partia) : that.partia != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (imie != null ? imie.hashCode() : 0);
        result = 31 * result + (nazwisko != null ? nazwisko.hashCode() : 0);
        result = 31 * result + (plec != null ? plec.hashCode() : 0);
        result = 31 * result + (zawod != null ? zawod.hashCode() : 0);
        result = 31 * result + (miejsceZamieszkania != null ? miejsceZamieszkania.hashCode() : 0);
        result = 31 * result + nrNaLiscie;
        result = 31 * result + (partia != null ? partia.hashCode() : 0);
        return result;
    }
}
