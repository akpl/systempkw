package pkw.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by Elimas on 2016-06-12.
 */
@Embeddable
public class WyboryOkregId implements Serializable {

    @Column(name = "WYBORY_ID")
    private int wyboryId;

    @Column(name = "OKREG_NR")
    private int okregNr;

    public int getWyboryId() {
        return wyboryId;
    }

    public void setWyboryId(int wyboryId) {
        this.wyboryId = wyboryId;
    }

    public int getOkregNr() {
        return okregNr;
    }

    public void setOkregNr(int okregNr) {
        this.okregNr = okregNr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WyboryOkregId that = (WyboryOkregId) o;

        if (wyboryId != that.wyboryId) return false;
        return okregNr == that.okregNr;

    }

    @Override
    public int hashCode() {
        int result = wyboryId;
        result = 31 * result + okregNr;
        return result;
    }
}
