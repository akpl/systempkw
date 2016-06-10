package pkw.controllers;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pkw.DaneWykresu;
import pkw.ImageNotFoundException;
import pkw.models.*;
import pkw.repositories.*;
import pkw.services.WynikiParlamentarneKalkulator;

import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

@Controller
public class MainController {



    @Autowired
    private PytanieReferendalneRepository pytanieReferendalneRepository;

    @Autowired
    private KandydatPrezydentRepository kandydatPrezydentRepository;

    @Autowired
    private KomisjaRepository komisjaRepository;

    @Autowired
    private WynikiParlamentarneKalkulator wynikiParlamentarneKalkulator;
    @Autowired
    private NewsletterRepository newsletterRepository;

    @Autowired
    private WyboryRepository wyboryRepository;

    @ModelAttribute("nextWybory")
    public Wybory nextWybory() {
        List<Wybory> wybory = wyboryRepository.findNextPrezydenckieOrParlamentarne();
        if (wybory.size() > 0) return wybory.get(0);
        else return null;
    }

    @ModelAttribute("nextParlamentarne")
    public Wybory nextParlamentarne() {
        List<Wybory> wybory = wyboryRepository.findNextParlamentarne();
        for (Wybory w : wybory) {
            for (Komitet komitet : w.getKomitety()) {
                for (KandydatPosel kp : komitet.getKandydaciPosel()) {
                    for (WynikiPosel wynikiPosel : kp.getWyniki()) {
                        if (wynikiPosel.getLiczbaGlosow() > 0) return w;
                    }
                }
            }
        }
        return null;
    }

    @ModelAttribute("nextPrezydenckie")
    public Wybory nextPrezydenckie() {
        List<Wybory> wybory = wyboryRepository.findNextPrezydenckie();
        for (Wybory w : wybory) {
            for (KandydatPrezydent kp : w.getKandydaciPrezydent()) {
                if (kp.getWynikLaczny().getLiczbaGlosow() > 0) return w;
            }
        }
        return null;
    }

    @ModelAttribute("nextReferendum")
    public Wybory nextReferendum() {
        List<Wybory> wybory = wyboryRepository.findNextReferendum();
        for (Wybory w : wybory) {
            for (PytanieReferendalne p : w.getPytaniaReferendalne()) {
                if (p.getWynikLaczny().getOdpowiedziNie() + p.getWynikLaczny().getOdpowiedziTak() > 0) return w;
            }
        }
        return null;
    }

    @RequestMapping("/")
    public String index(Newsletter newsletter) {
        Wybory w = nextParlamentarne();
        Wybory w2 = nextPrezydenckie();
        Wybory w3 = nextReferendum();
        return "public/index";
    }
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginForm() {
        return "login";
    }

    @RequestMapping(value = "/logout-success", method = RequestMethod.GET)
    public String logoutSuccess() {
        return "logout-success";
    }

    @RequestMapping(value = "/newsletter", method = RequestMethod.POST)
    public String signNewsletter(@Valid Newsletter newsletter, BindingResult bindingResult, Model model) {
        model.addAttribute("view", "newsletter");
        if (!bindingResult.hasErrors()) {
        }
        if (bindingResult.hasErrors()) {
            return "main";
        } else {
            newsletter.setEmail(newsletter.getEmail());
            newsletterRepository.save(newsletter);
            return "public/index";
        }
    }

    @RequestMapping(value = "/upload/prezydent/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getPrezydentImage(@PathVariable int id) throws FileNotFoundException {
        String path = "upload/prezydent/" + id + ".jpg";
        File file = new File(path);
        if (!file.exists()) throw new ImageNotFoundException();
        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.IMAGE_JPEG)
                .body(new InputStreamResource(new FileInputStream(file)));
    }

    @RequestMapping(value = "/upload/posel/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getPoselImage(@PathVariable int id) throws FileNotFoundException {
        String path = "upload/posel/" + id + ".jpg";
        File file = new File(path);
        if (!file.exists()) throw new ImageNotFoundException();
        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.IMAGE_JPEG)
                .body(new InputStreamResource(new FileInputStream(file)));
    }
    @RequestMapping("/parlamentarne")
    public  String parlamentarne(Model model )
    {
        przygotujWyniki(nextParlamentarne(), model );
        return "public/parlamentarne";
    }


    @RequestMapping("/prezydenckie")
    public  String prezydenckie(Model model)
    {
        przygotujWyniki(nextPrezydenckie(), model );
        return "public/prezydenckie";
    }

    @RequestMapping("/referendum")
    public  String referendum(Model model)
    {
        przygotujWyniki(nextReferendum(), model );
        return "public/referendum";
    }





    public void przygotujWyniki(Wybory wybory, Model model)
    {

        model.addAttribute("wybory", wybory);

        if(wybory.getFrekwencja() == null) {
            model.addAttribute("blad", "Brak wyników z wybranych wyborów.");
            model.addAttribute("view", "wyniki/lista");
            return;
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
    }
}