package pkw.models;

import java.util.List;

/**
 * Created by Elimas on 2016-01-07.
 */
public class WynikiPoselCollection {
    private List<WynikiPosel> kandydatPoselList;
    private List<Integer> kandydatPoselId;

    public List<WynikiPosel> getKandydatPoselList() {
        return kandydatPoselList;
    }

    public void setKandydatPoselList(List<WynikiPosel> kandydatPoselList) {
        this.kandydatPoselList = kandydatPoselList;
    }

    public List<Integer> getKandydatPoselId() {
        return kandydatPoselId;
    }

    public void setKandydatPoselId(List<Integer> kandydatPoselId) {
        this.kandydatPoselId = kandydatPoselId;
    }
}
