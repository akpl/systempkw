package pkw.models;

import pkw.DaneWykresu;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WynikiParlamentarne {
    private HashMap<Komitet,Integer> wyniki = new HashMap<>();

    public DaneWykresu getWykres() {
        DaneWykresu wykres = new DaneWykresu();
        for(Map.Entry<Komitet,Integer> komitet : wyniki.entrySet()){
            wykres.dodajElement(komitet.getKey().getNazwa(), komitet.getValue());
        }
        return wykres;
    }

    public Set<Map.Entry<Komitet, Integer>> getKomitety() {
        return wyniki.entrySet();
    }

    public void dodajWynik(Komitet komitet, int liczbaPoslow) {
        wyniki.put(komitet, wyniki.getOrDefault(komitet, 0) + liczbaPoslow);
    }
}
