package pkw;

import pkw.services.LabelValue;

import java.util.*;

public class DaneWykresu {
    private List<LabelValue> data = new ArrayList<>();

    public Collection<String> getLabels() {
        List<String> labels = new ArrayList<>();
        for(LabelValue lv : data)
        {
            labels.add(lv.getLabel());
        }
        return labels;
    }
    public Collection<Integer> getSeries() {
        List<Integer> series = new ArrayList<>();
        for(LabelValue lv : data)
        {
            series.add(lv.getValue());
        }
        return series;
    }

    public void dodajElement(String etykieta, Integer wartosc) {
        data.add(new LabelValue(etykieta, wartosc));
    }

    public List<LabelValue> getData() {
        return data;
    }
}
