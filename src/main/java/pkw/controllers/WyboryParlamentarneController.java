package pkw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pkw.models.*;

import javax.validation.Valid;

@Controller
public class WyboryParlamentarneController {
    @Autowired
    private WyboryRepository wyboryRepository;

    @Autowired
    private KomitetRepository komitetRepository;

    @Autowired
    private KandydatPoselRepository kandydatPoselRepository;

    @RequestMapping(value = "/election/szczegoly/komitety/dodaj", method = RequestMethod.GET)
    public String szczegolyKomitetyDodaj(@RequestParam(value = "idWybory") int idWybory, Komitet komitet, Model model) {
        model.addAttribute("view", "election/szczegoly/dodaj");
        model.addAttribute("exists", false);
        model.addAttribute("idWybory", idWybory);
        if (wyboryRepository.exists(idWybory)) {
            model.addAttribute("exists", true);
            model.addAttribute("wybory", wyboryRepository.findOne(idWybory));
        }
        return "main";
    }

    @RequestMapping(value = "/election/szczegoly/komitety/dodaj", method = RequestMethod.POST)
    public String szczegolyKomitetyDodaj(@RequestParam(value = "idWybory") int idWybory, @Valid Komitet komitet, BindingResult bindingResult, Model model) {
        model.addAttribute("view", "election/szczegoly/dodaj");
        model.addAttribute("exists", false);
        model.addAttribute("idWybory", idWybory);
        if (wyboryRepository.exists(idWybory)) {
            model.addAttribute("exists", true);
            model.addAttribute("wybory", wyboryRepository.findOne(idWybory));
            if (!bindingResult.hasErrors()) {
                if (komitetRepository.komitetExists(idWybory, komitet.getNazwa())) {
                    bindingResult.rejectValue("nazwa", "error.nazwa.exists", "Komitet o takiej nazwie już istnieje.");
                } else {
                    komitet.setWybory(wyboryRepository.findOne(idWybory));
                    komitetRepository.save(komitet);
                    //model.addAttribute("view", "election/szczegoly/index");
                    //model.addAttribute("success", true);
                    model.addAttribute("view", null);
                    model.addAttribute("exists", null);
                    model.addAttribute("wybory", null);
                    model.addAttribute("success", true);
                    return "redirect:/election/szczegoly";
                }
            }
        }
        return "main";
    }

    @RequestMapping(value = "/election/szczegoly/komitety/edycja", method = RequestMethod.GET)
    public String szczegolyKomitetyEdycja(@RequestParam(value = "idWybory") int idWybory, @RequestParam(value = "idKomitet") int idKomitet, Model model) {
        model.addAttribute("view", "election/szczegoly/edycja");
        model.addAttribute("edit", true);
        model.addAttribute("exists", false);
        model.addAttribute("idWybory", idWybory);
        model.addAttribute("idKomitet", idKomitet);
        if (wyboryRepository.exists(idWybory) && komitetRepository.exists(idKomitet)) {
            model.addAttribute("exists", true);
            model.addAttribute("wybory", wyboryRepository.findOne(idWybory));
            model.addAttribute("komitet", komitetRepository.findOne(idKomitet));
        }
        return "main";
    }

    @RequestMapping(value = "/election/szczegoly/komitety/edycja", method = RequestMethod.POST)
    public String szczegolyKomitetyEdycja(@RequestParam(value = "idWybory") int idWybory, @RequestParam(value = "idKomitet") int idKomitet, @Valid Komitet komitet, BindingResult bindingResult, Model model) {
        model.addAttribute("view", "election/szczegoly/edycja");
        model.addAttribute("edit", true);
        model.addAttribute("exists", false);
        model.addAttribute("idWybory", idWybory);
        model.addAttribute("idKomitet", idKomitet);
        if (wyboryRepository.exists(idWybory) && komitetRepository.exists(idKomitet)) {
            model.addAttribute("exists", true);
            model.addAttribute("wybory", wyboryRepository.findOne(idWybory));
            Komitet komitetDoEdycji = komitetRepository.findOne(idKomitet);
            if (!bindingResult.hasErrors()) {
                if (komitetRepository.komitetExists(idWybory, komitet.getNazwa()) && !komitet.getNazwa().equals(komitetDoEdycji.getNazwa())) {
                    bindingResult.rejectValue("nazwa", "error.nazwa.exists", "Komitet o takiej nazwie już istnieje.");
                } else {
                    komitet.setNr(komitetDoEdycji.getNr());
                    komitet.setWybory(wyboryRepository.findOne(idWybory));
                    komitetRepository.save(komitet);
                    model.addAttribute("view", null);
                    model.addAttribute("edit", null);
                    model.addAttribute("exists", null);
                    model.addAttribute("wybory", null);
                    model.addAttribute("idWybory", null);
                    model.addAttribute("idKomitet", null);
                    model.addAttribute("idWybory", idWybory);
                    model.addAttribute("success", true);
                    return "redirect:/election/szczegoly";
                }
            }
        }
        return "main";
    }

    @RequestMapping(value = "/election/szczegoly/komitety/usun")
    public String szczegolyKomitetyUsun(@RequestParam(value = "idWybory") int idWybory, @RequestParam(value = "idKomitet") int idKomitet, Model model) {
        model.addAttribute("view", "election/szczegoly/index");
        model.addAttribute("exists", false);
        model.addAttribute("success", false);
        if (komitetRepository.exists(idKomitet)) {
            model.addAttribute("exists", true);
            komitetRepository.delete(idKomitet);
            model.addAttribute("idKomitet", idKomitet);
            model.addAttribute("success", true);
            model.addAttribute("wybory", wyboryRepository.findOne(idWybory));
        } else {
            model.addAttribute("error", "Wybrane wybory nie istnieją.");
        }
        return "main";
    }

    @RequestMapping(value = "/election/szczegoly/komitety/poslowie")
    public String szczegolyKomitetyPoslowie(@RequestParam(value = "idWybory") int idWybory, @RequestParam(value = "idKomitet") int idKomitet, @RequestParam(value = "success", required = false, defaultValue = "false") boolean success, Model model) {
        model.addAttribute("view", "election/szczegoly/poslowie/index");
        model.addAttribute("exists", false);
        model.addAttribute("success", success);
        if (wyboryRepository.exists(idWybory) && komitetRepository.exists(idKomitet)) {
            model.addAttribute("exists", true);
            model.addAttribute("wybory", wyboryRepository.findOne(idWybory));
            model.addAttribute("komitet", komitetRepository.findOne(idKomitet));
        }
        return "main";
    }

    @RequestMapping(value = "/election/szczegoly/komitety/poslowie/dodaj", method = RequestMethod.GET)
    public String szczegolyKomitetyPoslowieDodaj(@RequestParam(value = "idWybory") int idWybory, @RequestParam(value = "idKomitet") int idKomitet, KandydatPosel kandydatPosel, Model model) {
        model.addAttribute("view", "election/szczegoly/poslowie/dodaj");
        model.addAttribute("exists", false);
        model.addAttribute("idWybory", idWybory);
        model.addAttribute("idKomitet", idKomitet);
        if (wyboryRepository.exists(idWybory) && komitetRepository.exists(idKomitet)) {
            model.addAttribute("exists", true);
            model.addAttribute("wybory", wyboryRepository.findOne(idWybory));
            model.addAttribute("komitet", komitetRepository.findOne(idKomitet));
            model.addAttribute("nrLista", kandydatPoselRepository.getNextNrListy(idKomitet));
        }
        return "main";
    }

    @RequestMapping(value = "/election/szczegoly/komitety/poslowie/dodaj", method = RequestMethod.POST)
    public String szczegolyKomitetyPoslowieDodaj(@RequestParam(value = "idWybory") int idWybory, @RequestParam(value = "idKomitet") int idKomitet, @Valid KandydatPosel kandydatPosel, BindingResult bindingResult, Model model) {
        model.addAttribute("view", "election/szczegoly/poslowie/dodaj");
        model.addAttribute("exists", false);
        model.addAttribute("idWybory", idWybory);
        model.addAttribute("idKomitet", idKomitet);
        if (wyboryRepository.exists(idWybory) && komitetRepository.exists(idKomitet)) {
            model.addAttribute("exists", true);
            model.addAttribute("wybory", wyboryRepository.findOne(idWybory));
            model.addAttribute("komitet", komitetRepository.findOne(idKomitet));
            if (!bindingResult.hasErrors()) {
                if (kandydatPoselRepository.kandydatPoselNrExists(idKomitet, kandydatPosel.getNrNaLiscie())) {
                    bindingResult.rejectValue("nrNaLiscie", "error.nrNaLiscie.exists", "Ten numer na liście jest już zajęty wybierz inny.");
                } else {
                    kandydatPosel.setKomitet(komitetRepository.findOne(idKomitet));
                    kandydatPoselRepository.save(kandydatPosel);
                    //model.addAttribute("view", "election/szczegoly/index");
                    //model.addAttribute("success", true);
                    model.addAttribute("view", null);
                    model.addAttribute("exists", null);
                    model.addAttribute("wybory", null);
                    model.addAttribute("success", true);
                    return "redirect:/election/szczegoly/komitety/poslowie";
                }
            }
        }
        return "main";
    }

    @RequestMapping(value = "/election/szczegoly/komitety/poslowie/edycja", method = RequestMethod.GET)
    public String szczegolyKomitetyPoslowieEdycja(@RequestParam(value = "idWybory") int idWybory, @RequestParam(value = "idKomitet") int idKomitet, @RequestParam(value = "idKandydatPosel") int idKandydatPosel, Model model) {
        model.addAttribute("view", "election/szczegoly/poslowie/edycja");
        model.addAttribute("edit", true);
        model.addAttribute("exists", false);
        model.addAttribute("idWybory", idWybory);
        model.addAttribute("idKomitet", idKomitet);
        model.addAttribute("idKandydatPosel", idKandydatPosel);
        if (wyboryRepository.exists(idWybory) && komitetRepository.exists(idKomitet) && kandydatPoselRepository.exists(idKandydatPosel)) {
            model.addAttribute("exists", true);
            model.addAttribute("wybory", wyboryRepository.findOne(idWybory));
            model.addAttribute("komitet", komitetRepository.findOne(idKomitet));
            model.addAttribute("kandydatPosel", kandydatPoselRepository.findOne(idKandydatPosel));
        }
        return "main";
    }

    @RequestMapping(value = "/election/szczegoly/komitety/poslowie/edycja", method = RequestMethod.POST)
    public String szczegolyKomitetyPoslowieEdycja(@RequestParam(value = "idWybory") int idWybory, @RequestParam(value = "idKomitet") int idKomitet, @RequestParam(value = "idKandydatPosel") int idKandydatPosel, @Valid KandydatPosel kandydatPosel, BindingResult bindingResult, Model model) {
        model.addAttribute("view", "election/szczegoly/poslowie/edycja");
        model.addAttribute("edit", true);
        model.addAttribute("exists", false);
        model.addAttribute("idWybory", idWybory);
        model.addAttribute("idKomitet", idKomitet);
        model.addAttribute("idKandydatPosel", idKandydatPosel);
        if (wyboryRepository.exists(idWybory) && komitetRepository.exists(idKomitet) && kandydatPoselRepository.exists(idKandydatPosel)) {
            model.addAttribute("exists", true);
            model.addAttribute("wybory", wyboryRepository.findOne(idWybory));
            model.addAttribute("komitet", komitetRepository.findOne(idKomitet));
            KandydatPosel kandydatPoselDoEdycji = kandydatPoselRepository.findOne(idKandydatPosel);
            if (!bindingResult.hasErrors()) {
                if (kandydatPoselRepository.kandydatPoselNrExists(idKomitet, kandydatPosel.getNrNaLiscie()) && kandydatPosel.getNrNaLiscie() != kandydatPoselDoEdycji.getNrNaLiscie()) {
                    bindingResult.rejectValue("nrNaLiscie", "error.nrNaLiscie.exists", "Ten numer na liście jest już zajęty wybierz inny.");
                } else {
                    kandydatPosel.setId(kandydatPoselDoEdycji.getId());
                    kandydatPosel.setKomitet(komitetRepository.findOne(idKomitet));
                    kandydatPoselRepository.save(kandydatPosel);
                    model.addAttribute("view", null);
                    model.addAttribute("edit", null);
                    model.addAttribute("exists", null);
                    model.addAttribute("wybory", null);
                    model.addAttribute("komitet", null);
                    model.addAttribute("idKandydatPosel", null);
                    model.addAttribute("idWybory", idWybory);
                    model.addAttribute("idKomitet", idKomitet);
                    model.addAttribute("success", true);
                    return "redirect:/election/szczegoly/komitety/poslowie";
                }
            }
        }
        return "main";
    }

    @RequestMapping(value = "/election/szczegoly/komitety/poslowie/usun")
    public String szczegolyKomitetyPoslowieUsun(@RequestParam(value = "idWybory") int idWybory, @RequestParam(value = "idKomitet") int idKomitet, @RequestParam(value = "idKandydatPosel") int idKandydatPosel,Model model) {
        model.addAttribute("view", "election/szczegoly/poslowie/index");
        model.addAttribute("exists", false);
        model.addAttribute("success", false);
        if (kandydatPoselRepository.exists(idKandydatPosel)) {
            model.addAttribute("exists", true);
            kandydatPoselRepository.delete(idKandydatPosel);
            model.addAttribute("idWybory", idWybory);
            model.addAttribute("idKomitet", idKomitet);
            model.addAttribute("success", true);
            model.addAttribute("wybory", wyboryRepository.findOne(idWybory));
            model.addAttribute("komitet", komitetRepository.findOne(idKomitet));
        } else {
            model.addAttribute("error", "Wybrane wybory nie istnieją.");
        }
        return "main";
    }
}
