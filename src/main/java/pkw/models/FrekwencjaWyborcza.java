package pkw.models;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

@Entity
@Table(name = "frekwencja_wyborcza")
public class FrekwencjaWyborcza {
    @Id
    @GeneratedValue
    @Column(name = "WYBORY_ID")
    private int id;

    @NotBlank
    @Column(name = "FREKWENCJA")
    private float frekwencja;

    public int getId() {
        return id;
    }

    public float getFrekwencja() {
        return frekwencja;
    }
}
