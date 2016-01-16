package pkw.controllers;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pkw.DaneWykresu;
import pkw.models.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class WynikiController {
    @Autowired
    private WyboryRepository wyboryRepository;

    @Autowired
    private PytanieReferendalneRepository pytanieReferendalneRepository;

    @Autowired
    private KandydatPrezydentRepository kandydatPrezydentRepository;

    @Autowired
    private WynikiParlamentarneRepository wynikiParlamentarneRepository;

    @Autowired
    private KomitetRepository komitetRepository;

    @ModelAttribute("wyboryList")
    public Iterable<Wybory> wyboryList() {
        return wyboryRepository.findByOrderByIdAsc();
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

        float frekwencja = wybory.getFrekwencja().getFrekwencja();
        float[] frekwencjaDane = {frekwencja, 100 - frekwencja};

        model.addAttribute("frekwencja", frekwencjaDane);
        DaneWykresu daneWykresu = new DaneWykresu();
        switch(wybory.getTypWyborow().getId())
        {
            case 1: //parl
                List<Komitet> komitety = komitetRepository.findByWybory(wybory);

                for(Komitet komitet : komitety) {
                    daneWykresu.dodajElement(komitet.getNazwa(), komitet.getWynikLaczny().getLiczbaPoslow());
                }

                model.addAttribute("wykres", daneWykresu);
                model.addAttribute("view", "wyniki/parlamentarne");
                break;
            case 2:
                List<KandydatPrezydent> kandydaci = kandydatPrezydentRepository.findByWybory(wybory);

                for(KandydatPrezydent kandydat : kandydaci) {
                    daneWykresu.dodajElement(kandydat.getImie() + " " + kandydat.getNazwisko(), kandydat.getWynikLaczny().getLiczbaGlosow());
                }

                model.addAttribute("wykres", daneWykresu);
                model.addAttribute("view", "wyniki/prezydent");
                break;
            case 3: //refer
                List<PytanieReferendalne> pytania = pytanieReferendalneRepository.findByWybory(wybory);
                List<List<Integer>> daneWykresow = new ArrayList<>();
                for (PytanieReferendalne pytanie : pytania) {
                    List<Integer> liczbaGlosow = new ArrayList<>();
                    WynikiPytaniaReferendalne wynikLaczny = pytanie.getWynikLaczny();
                    liczbaGlosow.add(wynikLaczny.getOdpowiedziTak());
                    liczbaGlosow.add(wynikLaczny.getOdpowiedziNie());
                    daneWykresow.add(liczbaGlosow);
                }
                model.addAttribute("pytania", pytania);
                model.addAttribute("wykresy", daneWykresow);
                model.addAttribute("view", "wyniki/referendum");
                break;
        }
        //model.addAttribute("view", "wyniki/wyswietl");
        return "main";
    }

}
