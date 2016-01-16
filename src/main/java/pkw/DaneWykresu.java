package pkw;

import java.util.ArrayList;
import java.util.List;

public class DaneWykresu {
    private List<String> labels = new ArrayList<>();
    private List<Integer> series = new ArrayList<>();

    public List<String> getLabels() {
        return labels;
    }

    public List<Integer> getSeries() {
        return series;
    }

    public void dodajElement(String etykieta, Integer wartosc) {
        labels.add(etykieta);
        series.add(wartosc);
    }
}
