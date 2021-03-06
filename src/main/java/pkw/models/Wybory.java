package pkw.models;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Range;
import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "wybory")
public class Wybory {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private int id;

    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @Column(name = "DATA_UTWORZENIA")
    private LocalDate dataUtworzenia;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    //@com.vcollaborate.validation.constraints.Future(today = true)
    @Column(name = "DATA_GLOSOWANIA")
    private LocalDate dataGlosowania;

    @Transient
    @NotNull
    @Range(min = 1, max = 3)
    private int typWyborowId = 1;

    @ManyToOne
    @JoinColumn(name = "TYP_WYBOROW_ID", referencedColumnName = "ID", nullable = false)
    private pkw.models.TypWyborow typWyborow;

    @ManyToOne
    @JoinColumn(name = "ID_TWORCY", referencedColumnName = "ID", nullable = false)
    private Uzytkownik tworca;

    @org.hibernate.annotations.OrderBy(clause = "ID ASC")
    @OneToMany(mappedBy = "wybory")
    private Set<PytanieReferendalne> pytaniaReferendalne;

    @org.hibernate.annotations.OrderBy(clause = "NR_NA_LISCIE ASC")
    @OneToMany(mappedBy = "wybory")
    private Set<KandydatPrezydent> kandydaciPrezydent;

    @org.hibernate.annotations.OrderBy(clause = "NR ASC")
    @OneToMany(mappedBy = "wybory")
    private Set<Komitet> komitety;

    @OneToOne
    @JoinColumn(name = "ID", referencedColumnName = "WYBORY_ID", nullable = false)
    private FrekwencjaWyborcza frekwencja;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDataUtworzenia() {
        return dataUtworzenia;
    }

    public void setDataUtworzenia(LocalDate dataUtworzenia) {
        this.dataUtworzenia = dataUtworzenia;
    }

    public LocalDate getDataGlosowania() {
        return dataGlosowania;
    }

    public void setDataGlosowania(LocalDate dataGlosowania) {
        this.dataGlosowania = dataGlosowania;
    }

    public int getTypWyborowId() {
        return typWyborowId;
    }

    public void setTypWyborowId(int typWyborowId) {
        this.typWyborowId = typWyborowId;
    }

    public pkw.models.TypWyborow getTypWyborow() {
        return typWyborow;
    }

    public void setTypWyborow(pkw.models.TypWyborow typWyborow) {
        this.typWyborow = typWyborow;
        this.typWyborowId = typWyborow.getId();
    }

    public Uzytkownik getTworca() {
        return tworca;
    }

    public void setTworca(Uzytkownik tworca) {
        this.tworca = tworca;
    }

    public Set<PytanieReferendalne> getPytaniaReferendalne() {
        return pytaniaReferendalne;
    }

    public void setPytaniaReferendalne(Set<PytanieReferendalne> pytaniaReferendalne) {
        this.pytaniaReferendalne = pytaniaReferendalne;
    }

    public Set<KandydatPrezydent> getKandydaciPrezydent() {
        return kandydaciPrezydent;
    }

    public void setKandydaciPrezydent(Set<KandydatPrezydent> kandydaciPrezydent) {
        this.kandydaciPrezydent = kandydaciPrezydent;
    }

    public Set<Komitet> getKomitety() {
        return komitety;
    }

    public void setKomitety(Set<Komitet> komitety) {
        this.komitety = komitety;
    }

    public FrekwencjaWyborcza getFrekwencja() {
        return frekwencja;
    }

    public void setFrekwencja(FrekwencjaWyborcza frekwencja) {
        this.frekwencja = frekwencja;
    }

    public boolean getCzySaWyniki(Komisja komisja) {
        boolean result = false;
        Wybory wybory = this;
        switch (wybory.getTypWyborow().getNazwa()) {
            case "PARLAMENTARNE":
                if (wybory.getKomitety() != null && wybory.getKomitety().size() > 0) {
                    for (Komitet komitet : wybory.getKomitety()) {
                        if (komitet.getKandydaciPosel() != null) {
                            for (KandydatPosel kandydatPosel : komitet.getKandydaciPosel()) {
                                if (kandydatPosel.getWynikiDlaKomisji(komisja) != null) {
                                    result = true;
                                } else return false;
                            }
                        }
                    }
                }
                break;
            case "PREZYDENCKIE":
                if (wybory.getKandydaciPrezydent() != null && wybory.getKandydaciPrezydent().size() > 0) {
                    for (KandydatPrezydent kandydatPrezydent : wybory.getKandydaciPrezydent()) {
                        if (kandydatPrezydent.getWynikiDlaKomisji(komisja) != null) {
                            result = true;
                        } else return false;
                    }
                }
                break;
            case "REFERENDUM":
                if (wybory.getPytaniaReferendalne() != null && wybory.getPytaniaReferendalne().size() > 0) {
                    for (PytanieReferendalne pytanie : wybory.getPytaniaReferendalne()) {
                        if (pytanie.getWynikiDlaKomisji(komisja) != null) {
                            result = true;
                        } else return false;
                    }
                }
                break;
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Wybory wybory = (Wybory) o;

        if (id != wybory.id) return false;
        if (dataUtworzenia != null ? !dataUtworzenia.equals(wybory.dataUtworzenia) : wybory.dataUtworzenia != null)
            return false;
        if (dataGlosowania != null ? !dataGlosowania.equals(wybory.dataGlosowania) : wybory.dataGlosowania != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (dataUtworzenia != null ? dataUtworzenia.hashCode() : 0);
        result = 31 * result + (dataGlosowania != null ? dataGlosowania.hashCode() : 0);
        return result;
    }
}
