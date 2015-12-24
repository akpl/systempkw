package pkw.models;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Elimas on 2015-12-24.
 */
@Entity
@Table(name = "TYPY_WYBOROW")
public class TypWyborow {
    private int id;
    private String nazwa;
    private List<pkw.models.Wybory> wybory;

    @Id
    @GeneratedValue(generator="TypWyborowId")
    @SequenceGenerator(name="TypWyborowId",sequenceName="typ_wyborow_seq")
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NotBlank
    @Column(name = "NAZWA")
    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TypWyborow that = (TypWyborow) o;

        if (id != that.id) return false;
        if (nazwa != null ? !nazwa.equals(that.nazwa) : that.nazwa != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (nazwa != null ? nazwa.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "typWyborow")
    public List<pkw.models.Wybory> getWybory() {
        return wybory;
    }

    public void setWybory(List<pkw.models.Wybory> wybory) {
        this.wybory = wybory;
    }
}
