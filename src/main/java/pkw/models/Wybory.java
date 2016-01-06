package pkw.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.hibernate.annotations.*;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Elimas on 2015-12-24.
 */
@Entity
@Table(name = "WYBORY")
public class Wybory {
    @Id
    @GeneratedValue(generator="WyboryId")
    @SequenceGenerator(name="WyboryId",sequenceName="wybory_seq")
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
    private List<PytanieReferendalne> pytaniaReferendalne;

    @org.hibernate.annotations.OrderBy(clause = "NR_NA_LISCIE ASC")
    @OneToMany(mappedBy = "wybory")
    private List<KandydatPrezydent> kandydaciPrezydent;

    @org.hibernate.annotations.OrderBy(clause = "NR ASC")
    @OneToMany(mappedBy = "wybory")
    private List<Komitet> komitety;

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

    public List<PytanieReferendalne> getPytaniaReferendalne() {
        return pytaniaReferendalne;
    }

    public void setPytaniaReferendalne(List<PytanieReferendalne> pytaniaReferendalne) {
        this.pytaniaReferendalne = pytaniaReferendalne;
    }

    public List<KandydatPrezydent> getKandydaciPrezydent() {
        return kandydaciPrezydent;
    }

    public void setKandydaciPrezydent(List<KandydatPrezydent> kandydaciPrezydent) {
        this.kandydaciPrezydent = kandydaciPrezydent;
    }

    public List<Komitet> getKomitety() {
        return komitety;
    }

    public void setKomitety(List<Komitet> komitety) {
        this.komitety = komitety;
    }

    public boolean getCzySaWyniki() {
        boolean result = false;
        Wybory wybory = this;
        switch (wybory.getTypWyborow().getNazwa()) {
            case "PARLAMENTARNE":
                if (wybory.getKomitety() != null && wybory.getKomitety().size() > 0) {
                    Komitet komitet = wybory.getKomitety().get(0);
                    if (komitet.getKandydaciPosel() != null && komitet.getKandydaciPosel().size() > 0) {
                        KandydatPosel posel = komitet.getKandydaciPosel().get(0);
                        if (posel.getWyniki() != null) result = true;
                    }
                }
                break;
            case "PREZYDENCKIE":
                if (wybory.getKandydaciPrezydent() != null && wybory.getKandydaciPrezydent().size() > 0) {
                    KandydatPrezydent prezydent = wybory.getKandydaciPrezydent().get(0);
                    if (prezydent.getWyniki() != null) result = true;
                }
                break;
            case "REFERENDUM":
                if (wybory.getPytaniaReferendalne() != null && wybory.getPytaniaReferendalne().size() > 0) {
                    PytanieReferendalne pytanie = wybory.getPytaniaReferendalne().get(0);
                    if (pytanie.getWyniki() != null) result = true;
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
