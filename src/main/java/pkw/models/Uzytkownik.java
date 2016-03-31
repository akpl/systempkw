package pkw.models;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by Elimas on 2015-12-23.
 */
@Entity
@Table(name = "UZYTKOWNICY")
public class Uzytkownik {
    private int id;
    private String login;
    private String haslo;
    private String imie;
    private String nazwisko;
    private String email;
    private int poziomDostepuId = 1;
    private PoziomDostepu poziomDostepu;
    private Komisja komisja;
    //private List<Wybory> wybory;

    @Id
    @GeneratedValue(generator="UzytkownicyId")
    @SequenceGenerator(name="UzytkownicyId",sequenceName="uzytkownicy_seq")
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NotBlank
    @Column(name = "LOGIN", unique = true)
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @NotBlank
    @Column(name = "HASLO")
    public String getHaslo() {
        return haslo;
    }

    public void setHaslo(String haslo) {
        this.haslo = haslo;
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

    @Transient
    @NotNull
    @Min(1)
    @Max(3)
    public int getPoziomDostepuId() {
        return poziomDostepuId;
    }

    public void setPoziomDostepuId(int poziomDostepuId) {
        this.poziomDostepuId = poziomDostepuId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Uzytkownik that = (Uzytkownik) o;

        if (id != that.id) return false;
        if (login != null ? !login.equals(that.login) : that.login != null) return false;
        if (haslo != null ? !haslo.equals(that.haslo) : that.haslo != null) return false;
        if (imie != null ? !imie.equals(that.imie) : that.imie != null) return false;
        if (nazwisko != null ? !nazwisko.equals(that.nazwisko) : that.nazwisko != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (haslo != null ? haslo.hashCode() : 0);
        result = 31 * result + (imie != null ? imie.hashCode() : 0);
        result = 31 * result + (nazwisko != null ? nazwisko.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "POZIOM_DOSTEPU_ID", referencedColumnName = "ID", nullable = false)
    public PoziomDostepu getPoziomDostepu() {
        return poziomDostepu;
    }

    public void setPoziomDostepu(PoziomDostepu poziomDostepu) {
        this.poziomDostepu = poziomDostepu;
        this.poziomDostepuId = poziomDostepu.getId();
    }

    @OneToOne(mappedBy = "przewodniczacy")
    public Komisja getKomisja() {
        return komisja;
    }

    public void setKomisja(Komisja komisja) {
        this.komisja = komisja;
    }

    @NotBlank
    @Email
    @Column(name = "EMAIL")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /*@OneToMany(mappedBy = "tworca")
    public List<Wybory> getWybory() {
        return wybory;
    }

    public void setWybory(List<Wybory> wybory) {
        this.wybory = wybory;
    }*/
}
