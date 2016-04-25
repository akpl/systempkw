package pkw.models;

import pkw.DaneWykresu;
import pkw.WynikiKomitetu;

import java.util.*;

public class WynikiParlamentarne {
    private HashMap<Komitet,Integer> wyniki = new HashMap<>();
    private final String[] koloryKomitetow = {"#FFCA08", "#E07C1C", "#00458A", "#AE046C", "#00A9E7", "#2F8F36"};

    public DaneWykresu getWykres() {
        DaneWykresu wykres = new DaneWykresu();
        for(Map.Entry<Komitet,Integer> komitet : wyniki.entrySet()){
            wykres.dodajElement(komitet.getKey().getNazwa(), komitet.getValue());
        }
        return wykres;
    }

    public Iterable<WynikiKomitetu> getKomitety() {
        List<WynikiKomitetu> komitety = new ArrayList<>();
        int i = 0;
        for(Map.Entry<Komitet,Integer> wynik : wyniki.entrySet()){
            komitety.add(new WynikiKomitetu(
                    wynik.getKey().getNazwa(),
                    wynik.getKey().getNr(),
                    wynik.getValue(),
                    koloryKomitetow[i % koloryKomitetow.length]));
            i++;
        }
        return komitety;
    }

    public void dodajWynik(Komitet komitet, int liczbaPoslow) {
        wyniki.put(komitet, wyniki.getOrDefault(komitet, 0) + liczbaPoslow);
    }
}
