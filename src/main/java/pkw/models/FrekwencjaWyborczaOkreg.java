package pkw.models;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

@Entity
@Table(name = "frekwencja_wyborcza_okreg")
public class FrekwencjaWyborczaOkreg {

    @EmbeddedId
    private WyboryOkregId id;

    @ManyToOne
    @JoinColumn(name = "WYBORY_ID", referencedColumnName = "ID", nullable = false, insertable = false, updatable = false)
    //@MapsId("WYBORY_ID")
    private Wybory wybory;

    @ManyToOne
    @JoinColumn(name = "OKREG_NR", referencedColumnName = "NR", nullable = false, insertable = false, updatable = false)
    //@MapsId("OKREG_NR")
    private Okreg okreg;

    @NotBlank
    @Column(name = "FREKWENCJA")
    private float frekwencja;

    public Wybory getWybory() {
        return wybory;
    }

    public void setWybory(Wybory wybory) {
        this.wybory = wybory;
    }

    public Okreg getOkreg() {
        return okreg;
    }

    public void setOkreg(Okreg okreg) {
        this.okreg = okreg;
    }

    public float getFrekwencja() {
        return frekwencja;
    }

    public void setFrekwencja(float frekwencja) {
        this.frekwencja = frekwencja;
    }
}
