package pkw.controllers;

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
import pkw.models.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProtokolController {
    @Autowired
    private WyboryRepository wyboryRepository;

    @Autowired
    private UzytkownikRepository uzytkownikRepository;

    @Autowired
    private WynikiPytaniaReferendalneRepository wynikiPytaniaReferendalneRepository;

    @ModelAttribute("dzisiejszeWybory")
    public Iterable<Wybory> dzisiejszeWybory() {
        return wyboryRepository.dzisiejszeWybory(new LocalDate());
    }

    /*@ModelAttribute("czySaWyniki")
    public boolean czySaWyniki(int wyboryId) {
        boolean result = false;
        Wybory wybory = wyboryRepository.findOne(wyboryId);
        switch (wybory.getTypWyborow().getNazwa()) {
            case "PARLAMENTARNE":
                if (wybory.getKomitety() != null && wybory.getKomitety().size() > 0) {
                    Komitet komitet = wybory.getKomitety().get(0);
                    if (komitet.getKandydaciPosel() != null && komitet.getKandydaciPosel().size() > 0) {
                        KandydatPosel posel = komitet.getKandydaciPosel().get(0);
                        if (posel.getWyniki() != null) result = true;
                    }
                }
                break;
            case "PREZYDENCKIE":
                if (wybory.getKandydaciPrezydent() != null && wybory.getKandydaciPrezydent().size() > 0) {
                    KandydatPrezydent prezydent = wybory.getKandydaciPrezydent().get(0);
                    if (prezydent.getWyniki() != null) result = true;
                }
                break;
            case "REFERENDUM":
                if (wybory.getPytaniaReferendalne() != null && wybory.getPytaniaReferendalne().size() > 0) {
                    PytanieReferendalne pytanie = wybory.getPytaniaReferendalne().get(0);
                    if (pytanie.getWyniki() != null) result = true;
                }
                break;
        }
        return result;
    }*/

    @RequestMapping(value = "/protokoly", method = RequestMethod.GET)
    public String index(@RequestParam(value = "success", required = false, defaultValue = "false") boolean success, Model model) {
        model.addAttribute("view", "protokoly/index");
        model.addAttribute("success", success);
        model.addAttribute("authorized", false);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Uzytkownik loggedUser = uzytkownikRepository.findByLogin(user.getUsername()).get(0);
        if (loggedUser.getKomisja() != null) {
            model.addAttribute("authorized", true);
            model.addAttribute("komisja", loggedUser.getKomisja());
        }
        return "main";
    }

    @RequestMapping(value = "/protokoly/wyslij", method = RequestMethod.GET)
    public String wyslij(@RequestParam(value = "idWybory") int idWybory, WynikiPytaniaReferendalneCollection wynikiPytaniaReferendalneCollection, Model model) {
        model.addAttribute("view", "protokoly/wyslij");
        model.addAttribute("exists", false);
        model.addAttribute("authorized", false);
        if (wyboryRepository.exists(idWybory)) {
            Wybory wybory = wyboryRepository.findOne(idWybory);
            model.addAttribute("exists", true);
            model.addAttribute("wybory", wybory);
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Uzytkownik loggedUser = uzytkownikRepository.findByLogin(user.getUsername()).get(0);
            if (loggedUser.getKomisja() != null) {
                model.addAttribute("authorized", true);
                model.addAttribute("komisja", loggedUser.getKomisja());
                if (wybory.getTypWyborow().getNazwa().equals("REFERENDUM")) {
                    model.addAttribute("pytaniaReferendalne", wybory.getPytaniaReferendalne());
                    ArrayList<WynikiPytaniaReferendalne> wynikiList = new ArrayList<>();
                    for (PytanieReferendalne pytanie : wybory.getPytaniaReferendalne()) {
                        wynikiList.add(wynikiPytaniaReferendalneRepository.findOneByPytanieReferendalneAndKomisja(pytanie, loggedUser.getKomisja()));
                    }
                    wynikiPytaniaReferendalneCollection.setPytaniaReferendalneList(wynikiList);
                    model.addAttribute("wynikiPytaniaReferendalneCollection", wynikiPytaniaReferendalneCollection);
                }
            }
        }
        return "main";
    }

    @RequestMapping(value = "/protokoly/wyslij/referendum", method = RequestMethod.POST)
    public String wyslijReferendum(@RequestParam(value = "idWybory") int idWybory, @Valid WynikiPytaniaReferendalneCollection wynikiPytaniaReferendalneCollection, BindingResult bindingResult, Model model) {
        model.addAttribute("view", "protokoly/wyslij");
        model.addAttribute("exists", false);
        model.addAttribute("authorized", false);
        model.addAttribute("success", false);
        model.addAttribute("errorVotes", false);
        if (wyboryRepository.exists(idWybory)) {
            Wybory wybory = wyboryRepository.findOne(idWybory);
            model.addAttribute("exists", true);
            model.addAttribute("wybory", wybory);
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Uzytkownik loggedUser = uzytkownikRepository.findByLogin(user.getUsername()).get(0);
            if (loggedUser.getKomisja() != null) {
                model.addAttribute("authorized", true);
                model.addAttribute("komisja", loggedUser.getKomisja());
                if (wybory.getTypWyborow().getNazwa().equals("REFERENDUM")) {
                    model.addAttribute("pytaniaReferendalne", wybory.getPytaniaReferendalne());
                    if (!bindingResult.hasErrors()) {
                        boolean errorVotes = false;
                        for (WynikiPytaniaReferendalne wyniki : wynikiPytaniaReferendalneCollection.getPytaniaReferendalneList()) {
                            int liczbaGlosow = wyniki.getOdpowiedziNie() + wyniki.getOdpowiedziTak();
                            if (liczbaGlosow > loggedUser.getKomisja().getLiczbaWyborcow()) {
                                errorVotes = true;
                                break;
                            }
                        }
                        if (errorVotes) {
                            model.addAttribute("errorVotes", true);
                        } else {
                            int i = 0;
                            for (WynikiPytaniaReferendalne wyniki : wynikiPytaniaReferendalneCollection.getPytaniaReferendalneList()) {
                                wyniki.setPytanieReferendalne(wybory.getPytaniaReferendalne().get(i));
                                wyniki.setKomisja(loggedUser.getKomisja());
                                WynikiPytaniaReferendalne wynikiEdycja = wynikiPytaniaReferendalneRepository.findOneByPytanieReferendalneAndKomisja(wyniki.getPytanieReferendalne(), wyniki.getKomisja());
                                if (wynikiEdycja != null) {
                                    wyniki.setId(wynikiEdycja.getId());
                                }
                                wynikiPytaniaReferendalneRepository.save(wyniki);
                                i++;
                            }
                            model.addAttribute("view", null);
                            model.addAttribute("exists", null);
                            model.addAttribute("authorized", null);
                            model.addAttribute("errorVotes", null);
                            model.addAttribute("wybory", null);
                            model.addAttribute("komisja", null);
                            model.addAttribute("pytaniaReferendalne", null);
                            model.addAttribute("success", true);
                            return "redirect:/protokoly";
                        }
                    }
                }
            }
        }
        return "main";
    }
}