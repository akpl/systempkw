package pkw.models;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

/**
 * Created by Elimas on 2015-12-27.
 */
@Entity
@Table(name = "PYTANIA_REFERENDALNE")
public class PytanieReferendalne {
    private int id;
    private String pytanie;
    private Wybory wybory;

    @Id
    @GeneratedValue(generator="PytaniaReferendalneId")
    @SequenceGenerator(name="PytaniaReferendalneId",sequenceName="pytania_referendalne_seq")
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NotBlank
    @Column(name = "PYTANIE")
    public String getPytanie() {
        return pytanie;
    }

    public void setPytanie(String pytanie) {
        this.pytanie = pytanie;
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

        PytanieReferendalne that = (PytanieReferendalne) o;

        if (id != that.id) return false;
        if (pytanie != null ? !pytanie.equals(that.pytanie) : that.pytanie != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (pytanie != null ? pytanie.hashCode() : 0);
        return result;
    }
}
