package pkw.controllers;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pkw.DaneWykresu;
import pkw.models.*;
import pkw.repositories.*;
import pkw.services.WynikiParlamentarneKalkulator;

import java.util.*;

@Controller
@RequestMapping("/panel")
public class WynikiController {
    @Autowired
    private WyboryRepository wyboryRepository;

    @Autowired
    private PytanieReferendalneRepository pytanieReferendalneRepository;

    @Autowired
    private KandydatPrezydentRepository kandydatPrezydentRepository;

    @Autowired
    private KomisjaRepository komisjaRepository;

    @Autowired
    private WynikiParlamentarneKalkulator wynikiParlamentarneKalkulator;


    @ModelAttribute("wyboryList")
    public Iterable<Wybory> wyboryList() {
        return wyboryRepository.findByDataGlosowaniaBeforeOrderByIdAsc(new LocalDate().plusDays(1));
    }

    @RequestMapping(value = "/wyniki")
    public String lista(Model model) {
        model.addAttribute("view", "wyniki/lista");
        return "main";
    }

    @RequestMapping(value = "/wyniki/wyswietl")
    public String wyswietl(Model model, @RequestParam(value = "idWybory") int idWybory) {
        Wybory wybory = wyboryRepository.findOne(idWybory);
        model.addAttribute("wybory", wybory);

        if(wybory.getFrekwencja() == null) {
            model.addAttribute("blad", "Brak wyników z wybranych wyborów.");
            model.addAttribute("view", "wyniki/lista");
            return "main";
        }
        float frekwencja = wybory.getFrekwencja().getFrekwencja();
        float[] frekwencjaDane = {frekwencja, 100 - frekwencja};

        model.addAttribute("frekwencja", frekwencjaDane);
        switch(wybory.getTypWyborow().getNazwa())
        {
            case "PARLAMENTARNE": {
                WynikiParlamentarne wyniki = wynikiParlamentarneKalkulator.obliczDlaWyborow(wybory);

                model.addAttribute("komitety", wyniki.getKomitety());
                model.addAttribute("wykres", wyniki.getWykres());
                model.addAttribute("view", "wyniki/parlamentarne");
                break;
            }
            case "PREZYDENCKIE": {
                List<KandydatPrezydent> kandydaci = kandydatPrezydentRepository.findByWybory(wybory);
                SortedMap<LocalDateTime, Integer> liczbaGlosow = new TreeMap<>();
                DaneWykresu daneWykresu = new DaneWykresu();

                for (KandydatPrezydent kandydat : kandydaci) {
                    daneWykresu.dodajElement(kandydat.getImie() + " " + kandydat.getNazwisko(), kandydat.getWynikLaczny().getLiczbaGlosow());
                    for (WynikiPrezydent wyniki : kandydat.getWyniki()) {
                        Integer glosy = liczbaGlosow.getOrDefault(wyniki.getCzasWprowadzenia(), 0);
                        glosy += wyniki.getLiczbaGlosow();
                        liczbaGlosow.put(wyniki.getCzasWprowadzenia(), glosy);
                    }
                }

                DaneWykresu frekwencjaWCzasie = new DaneWykresu();
                int sumaGlosow = 0;
                int liczbaWyborcow = komisjaRepository.getLiczbaWyborcow();
                for(Map.Entry<LocalDateTime, Integer> punktCzasu : liczbaGlosow.entrySet()) {
                    sumaGlosow += punktCzasu.getValue();
                    Double f = (double)sumaGlosow / liczbaWyborcow * 100;
                    frekwencjaWCzasie.dodajElement(punktCzasu.getKey().toString(), f.intValue());
                }

                model.addAttribute("frekwencjaWCzasie", frekwencjaWCzasie);
                model.addAttribute("kandydaci", kandydaci);
                model.addAttribute("wykres", daneWykresu);
                model.addAttribute("view", "wyniki/prezydent");
                break;
            }
            case "REFERENDUM": {
                List<PytanieReferendalne> pytania = pytanieReferendalneRepository.findByWybory(wybory);
                List<List<Integer>> daneWykresow = new ArrayList<>();
                SortedMap<LocalDateTime, Integer> liczbaGlosow = new TreeMap<>();

                for (PytanieReferendalne pytanie : pytania) {
                    List<Integer> glosy = new ArrayList<>();
                    WynikiPytaniaReferendalne wynikLaczny = pytanie.getWynikLaczny();
                    glosy.add(wynikLaczny.getOdpowiedziTak());
                    glosy.add(wynikLaczny.getOdpowiedziNie());
                    daneWykresow.add(glosy);
                }

                if(!pytania.isEmpty()) {
                    for (WynikiPytaniaReferendalne wyniki : pytania.get(0).getWyniki()) {
                        Integer gl = liczbaGlosow.getOrDefault(wyniki.getCzasWprowadzenia(), 0);
                        gl += wyniki.getOdpowiedziTak();
                        gl += wyniki.getOdpowiedziNie();
                        liczbaGlosow.put(wyniki.getCzasWprowadzenia(), gl);
                    }
                }
                DaneWykresu frekwencjaWCzasie = new DaneWykresu();
                int sumaGlosow = 0;
                int liczbaWyborcow = komisjaRepository.getLiczbaWyborcow();
                for(Map.Entry<LocalDateTime, Integer> punktCzasu : liczbaGlosow.entrySet()) {
                    sumaGlosow += punktCzasu.getValue();
                    Double f = (double)sumaGlosow / liczbaWyborcow * 100;
                    frekwencjaWCzasie.dodajElement(punktCzasu.getKey().toString(), f.intValue());
                }

                model.addAttribute("frekwencjaWCzasie", frekwencjaWCzasie);
                model.addAttribute("pytania", pytania);
                model.addAttribute("wykresy", daneWykresow);
                model.addAttribute("view", "wyniki/referendum");
                break;
            }
        }
        return "main";
    }

}