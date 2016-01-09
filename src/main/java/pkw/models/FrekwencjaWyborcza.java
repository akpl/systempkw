package pkw.models;

import org.hibernate.annotations.*;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "FREKWENCJA_WYBORCZA")
public class FrekwencjaWyborcza {
    @Id
    @GeneratedValue(generator="WyboryId")
    @SequenceGenerator(name="WyboryId",sequenceName="wybory_seq")
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