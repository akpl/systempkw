package pkw.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pkw.models.*;
import pkw.repositories.OkregRepository;
import pkw.repositories.WynikiPoselRepository;

import java.util.*;

@Service
public class WynikiParlamentarneKalkulator {
    public final double progWyborczy = 0.05;

    @Autowired
    private OkregRepository okregRepository;

    @Autowired
    private WynikiPoselRepository wynikiPoselRepository;

    public WynikiParlamentarne obliczDlaWyborow(Wybory wybory) {
        Iterable<Okreg> okregi = okregRepository.findAll();

        WynikiParlamentarne wynikiKoncowe = new WynikiParlamentarne();
        int minimalnaLiczbaGlosow = znajdzMinimalnaLiczbeGlosow(wybory);

        for(Okreg okreg : okregi) {
            obliczWynikiWOkregu(wynikiKoncowe, wybory, okreg, minimalnaLiczbaGlosow);
        }
        return wynikiKoncowe;
    }

    private void obliczWynikiWOkregu(WynikiParlamentarne wynikiKoncowe, Wybory wybory, Okreg okreg, int minimalnaLiczbaGlosow) {
        Iterable<WynikiPosel> wyniki = wynikiPoselRepository.
                findByKomisja_OkregWyborczyAndKandydatPosel_Komitet_Wybory(okreg, wybory);

        HashMap<Komitet,Integer> wynikiKomitetow = new HashMap<>();
        for(WynikiPosel wynik : wyniki) {
            Komitet komitet = wynik.getKandydatPosel().getKomitet();
            wynikiKomitetow.put(komitet, wynikiKomitetow.getOrDefault(komitet, 0) + wynik.getLiczbaGlosow());
        }

        int liczbaMandatow = okreg.getLiczbaMandatow();

        List<WspolczynnikKomitetu> wspolczynniki = new ArrayList<>();

        for(Map.Entry<Komitet,Integer> komitet : wynikiKomitetow.entrySet()) {
            if(komitet.getValue() >= minimalnaLiczbaGlosow) {
                for (int dzielnik = 1; dzielnik < liczbaMandatow; dzielnik++) {
                    int wspolczynnik = komitet.getValue() / dzielnik;
                    wspolczynniki.add(new WspolczynnikKomitetu(komitet.getKey(), wspolczynnik));
                }
            } else {
                wynikiKoncowe.dodajWynik(komitet.getKey(), 0);
            }
        }

        Collections.sort(wspolczynniki);
        for (int mandat = 0; mandat < liczbaMandatow; mandat++) {
            WspolczynnikKomitetu wspolczynnik = wspolczynniki.get(mandat);
            wynikiKoncowe.dodajWynik(wspolczynnik.getKomitet(), 1);
        }
    }

    class WspolczynnikKomitetu implements Comparable<WspolczynnikKomitetu> {
        private Komitet komitet;
        private int wspolczynnik;

        public WspolczynnikKomitetu(Komitet komitet, int wspolczynnik) {
            this.komitet = komitet;
            this.wspolczynnik = wspolczynnik;
        }

        public Komitet getKomitet() {
            return komitet;
        }

        public int getWspolczynnik() {
            return wspolczynnik;
        }

        @Override
        public int compareTo(WspolczynnikKomitetu wspolczynnikKomitetu) {
            if(wspolczynnik < wspolczynnikKomitetu.getWspolczynnik())
                return 1;
            else if(wspolczynnik == wspolczynnikKomitetu.getWspolczynnik())
                return 0;
            return -1;
        }
    }

    private int znajdzMinimalnaLiczbeGlosow(Wybory wybory) {
        double minimalnaLiczbaWyborcow = progWyborczy * znajdzLiczbeWyborcow(wybory);
        return (int)minimalnaLiczbaWyborcow; //zaokrąglenie w dół do całości
    }

    private int znajdzLiczbeWyborcow(Wybory wybory) {
        Iterable<WynikiPosel> wyniki = wynikiPoselRepository.findByKandydatPosel_Komitet_Wybory(wybory);
        int liczbaWyborcow = 0;

        for(WynikiPosel wynik : wyniki) {
            liczbaWyborcow += wynik.getLiczbaGlosow();
        }

        return liczbaWyborcow;
    }
}
