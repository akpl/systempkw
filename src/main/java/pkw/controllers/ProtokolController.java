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
import pkw.Counter;
import pkw.models.*;
import pkw.repositories.*;

import javax.validation.Valid;
import java.util.ArrayList;

@Controller
public class ProtokolController {
    @Autowired
    private WyboryRepository wyboryRepository;

    @Autowired
    private UzytkownikRepository uzytkownikRepository;

    @Autowired
    private WynikiPytaniaReferendalneRepository wynikiPytaniaReferendalneRepository;

    @Autowired
    private WynikiPrezydentRepository wynikiPrezydentRepository;

    @Autowired
    private WynikiPoselRepository wynikiPoselRepository;

    @Autowired
    private KandydatPoselRepository kandydatPoselRepository;

    @ModelAttribute("dzisiejszeWybory")
    public Iterable<Wybory> dzisiejszeWybory() {
        return wyboryRepository.dzisiejszeWybory(new LocalDate());
    }

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
    public String wyslij(@RequestParam(value = "idWybory") int idWybory, WynikiPytaniaReferendalneCollection wynikiPytaniaReferendalneCollection, WynikiPrezydentCollection wynikiPrezydentCollection, WynikiPoselCollection wynikiPoselCollection, Model model) {
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
                model.addAttribute("counter", new Counter());
                if (wybory.getTypWyborow().getNazwa().equals("REFERENDUM")) {
                    model.addAttribute("pytaniaReferendalne", wybory.getPytaniaReferendalne());
                    ArrayList<WynikiPytaniaReferendalne> wynikiList = new ArrayList<>();
                    for (PytanieReferendalne pytanie : wybory.getPytaniaReferendalne()) {
                        wynikiList.add(wynikiPytaniaReferendalneRepository.findOneByPytanieReferendalneAndKomisja(pytanie, loggedUser.getKomisja()));
                    }
                    wynikiPytaniaReferendalneCollection.setPytaniaReferendalneList(wynikiList);
                    model.addAttribute("wynikiPytaniaReferendalneCollection", wynikiPytaniaReferendalneCollection);
                } else if (wybory.getTypWyborow().getNazwa().equals("PREZYDENCKIE")) {
                    model.addAttribute("kandydaciPrezydent", wybory.getKandydaciPrezydent());
                    ArrayList<WynikiPrezydent> wynikiList = new ArrayList<>();
                    for (KandydatPrezydent prezydent : wybory.getKandydaciPrezydent()) {
                        wynikiList.add(wynikiPrezydentRepository.findOneByKandydatPrezydentAndKomisja(prezydent, loggedUser.getKomisja()));
                    }
                    wynikiPrezydentCollection.setKandydatPrezydentList(wynikiList);
                    model.addAttribute("wynikiPrezydentCollection", wynikiPrezydentCollection);
                } else if (wybory.getTypWyborow().getNazwa().equals("PARLAMENTARNE")) {
                    model.addAttribute("komitety", wybory.getKomitety());
                    ArrayList<WynikiPosel> wynikiList = new ArrayList<>();
                    ArrayList<Integer> kandydatPoselId = new ArrayList<>();
                    for (Komitet komitet : wybory.getKomitety()) {
                        for (KandydatPosel posel : komitet.getKandydaciPosel()) {
                            wynikiList.add(wynikiPoselRepository.findOneByKandydatPoselAndKomisja(posel, loggedUser.getKomisja()));
                            kandydatPoselId.add(posel.getId());
                        }
                    }
                    wynikiPoselCollection.setKandydatPoselList(wynikiList);
                    wynikiPoselCollection.setKandydatPoselId(kandydatPoselId);
                    model.addAttribute("wynikiPoselCollection", wynikiPoselCollection);
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
                                wyniki.setPytanieReferendalne((PytanieReferendalne) wybory.getPytaniaReferendalne().toArray()[i]);
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

    @RequestMapping(value = "/protokoly/wyslij/prezydenckie", method = RequestMethod.POST)
    public String wyslijPrezydenckie(@RequestParam(value = "idWybory") int idWybory, @Valid WynikiPrezydentCollection wynikiPrezydentCollection, BindingResult bindingResult, Model model) {
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
                if (wybory.getTypWyborow().getNazwa().equals("PREZYDENCKIE")) {
                    model.addAttribute("kandydaciPrezydent", wybory.getKandydaciPrezydent());
                    if (!bindingResult.hasErrors()) {
                        boolean errorVotes = false;
                        int liczbaGlosow = 0;
                        for (WynikiPrezydent wyniki : wynikiPrezydentCollection.getKandydatPrezydentList()) {
                            liczbaGlosow += wyniki.getLiczbaGlosow();
                        }
                        if (liczbaGlosow > loggedUser.getKomisja().getLiczbaWyborcow()) {
                            errorVotes = true;
                        }
                        if (errorVotes) {
                            model.addAttribute("errorVotes", true);
                        } else {
                            int i = 0;
                            for (WynikiPrezydent wyniki : wynikiPrezydentCollection.getKandydatPrezydentList()) {
                                wyniki.setKandydatPrezydent((KandydatPrezydent) wybory.getKandydaciPrezydent().toArray()[i]);
                                wyniki.setKomisja(loggedUser.getKomisja());
                                WynikiPrezydent wynikiEdycja = wynikiPrezydentRepository.findOneByKandydatPrezydentAndKomisja(wyniki.getKandydatPrezydent(), wyniki.getKomisja());
                                if (wynikiEdycja != null) {
                                    wyniki.setId(wynikiEdycja.getId());
                                }
                                wynikiPrezydentRepository.save(wyniki);
                                i++;
                            }
                            model.addAttribute("view", null);
                            model.addAttribute("exists", null);
                            model.addAttribute("authorized", null);
                            model.addAttribute("errorVotes", null);
                            model.addAttribute("wybory", null);
                            model.addAttribute("komisja", null);
                            model.addAttribute("kandydaciPrezydent", null);
                            model.addAttribute("success", true);
                            return "redirect:/protokoly";
                        }
                    }
                }
            }
        }
        return "main";
    }

    @RequestMapping(value = "/protokoly/wyslij/parlamentarne", method = RequestMethod.POST)
    public String wyslijParlamentarne(@RequestParam(value = "idWybory") int idWybory, @Valid WynikiPoselCollection wynikiPoselCollection, BindingResult bindingResult, Model model) {
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
                model.addAttribute("counter", new Counter());
                if (wybory.getTypWyborow().getNazwa().equals("PARLAMENTARNE")) {
                    model.addAttribute("komitety", wybory.getKomitety());
                    if (!bindingResult.hasErrors()) {
                        boolean errorVotes = false;
                        int liczbaGlosow = 0;
                        for (WynikiPosel wyniki : wynikiPoselCollection.getKandydatPoselList()) {
                            liczbaGlosow += wyniki.getLiczbaGlosow();
                        }
                        if (liczbaGlosow > loggedUser.getKomisja().getLiczbaWyborcow()) {
                            errorVotes = true;
                        }
                        if (errorVotes) {
                            model.addAttribute("errorVotes", true);
                        } else {
                            int i = 0;
                            for (WynikiPosel wyniki : wynikiPoselCollection.getKandydatPoselList()) {
                                wyniki.setKandydatPosel(kandydatPoselRepository.findOne(wynikiPoselCollection.getKandydatPoselId().get(i)));
                                wyniki.setKomisja(loggedUser.getKomisja());
                                WynikiPosel wynikiEdycja = wynikiPoselRepository.findOneByKandydatPoselAndKomisja(wyniki.getKandydatPosel(), wyniki.getKomisja());
                                if (wynikiEdycja != null) {
                                    wyniki.setId(wynikiEdycja.getId());
                                }
                                wynikiPoselRepository.save(wyniki);
                                i++;
                            }
                            model.addAttribute("view", null);
                            model.addAttribute("exists", null);
                            model.addAttribute("authorized", null);
                            model.addAttribute("errorVotes", null);
                            model.addAttribute("wybory", null);
                            model.addAttribute("komisja", null);
                            model.addAttribute("kandydaciPrezydent", null);
                            model.addAttribute("counter", null);
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
