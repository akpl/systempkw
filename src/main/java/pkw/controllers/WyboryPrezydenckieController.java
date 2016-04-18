package pkw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pkw.models.KandydatPrezydent;
import pkw.repositories.KandydatPrezydentRepository;
import pkw.repositories.WyboryRepository;

import javax.validation.Valid;

@Controller
@RequestMapping("/panel")
public class WyboryPrezydenckieController {
    @Autowired
    private WyboryRepository wyboryRepository;

    @Autowired
    private KandydatPrezydentRepository kandydatPrezydentRepository;

    @RequestMapping(value = "/wybory/szczegoly/prezydenckie/dodaj", method = RequestMethod.GET)
    public String szczegolyPrezydenckieDodaj(@RequestParam(value = "idWybory") int idWybory, KandydatPrezydent kandydatPrezydent, Model model) {
        model.addAttribute("view", "wybory/szczegoly/dodaj");
        model.addAttribute("exists", false);
        model.addAttribute("idWybory", idWybory);
        if (wyboryRepository.exists(idWybory)) {
            model.addAttribute("exists", true);
            model.addAttribute("wybory", wyboryRepository.findOne(idWybory));
            model.addAttribute("nrLista", kandydatPrezydentRepository.getNextNrListy(idWybory));
        }
        return "main";
    }

    @RequestMapping(value = "/wybory/szczegoly/prezydenckie/dodaj", method = RequestMethod.POST)
    public String szczegolyPrezydenckieDodaj(@RequestParam(value = "idWybory") int idWybory, @Valid KandydatPrezydent kandydatPrezydent, BindingResult bindingResult, Model model) {
        model.addAttribute("view", "wybory/szczegoly/dodaj");
        model.addAttribute("exists", false);
        model.addAttribute("idWybory", idWybory);
        if (wyboryRepository.exists(idWybory)) {
            model.addAttribute("exists", true);
            model.addAttribute("wybory", wyboryRepository.findOne(idWybory));
            if (!bindingResult.hasErrors()) {
                if (kandydatPrezydentRepository.kandydatPrezydentExists(idWybory, kandydatPrezydent.getNrNaLiscie())) {
                    bindingResult.rejectValue("nrNaLiscie", "error.nrNaLiscie.exists", "Ten numer na liście jest już zajęty wybierz inny.");
                } else {
                    kandydatPrezydent.setWybory(wyboryRepository.findOne(idWybory));
                    kandydatPrezydentRepository.save(kandydatPrezydent);
                    //model.addAttribute("view", "wybory/szczegoly/index");
                    //model.addAttribute("success", true);
                    model.addAttribute("view", null);
                    model.addAttribute("exists", null);
                    model.addAttribute("wybory", null);
                    model.addAttribute("success", true);
                    return "redirect:/wybory/szczegoly";
                }
            }
        }
        return "main";
    }

    @RequestMapping(value = "/wybory/szczegoly/prezydenckie/edycja", method = RequestMethod.GET)
    public String szczegolyPrezydenckieEdycja(@RequestParam(value = "idWybory") int idWybory, @RequestParam(value = "idKandydatPrezydent") int idKandydatPrezydent, Model model) {
        model.addAttribute("view", "wybory/szczegoly/edycja");
        model.addAttribute("edit", true);
        model.addAttribute("exists", false);
        model.addAttribute("idWybory", idWybory);
        model.addAttribute("idKandydatPrezydent", idKandydatPrezydent);
        if (wyboryRepository.exists(idWybory) && kandydatPrezydentRepository.exists(idKandydatPrezydent)) {
            model.addAttribute("exists", true);
            model.addAttribute("wybory", wyboryRepository.findOne(idWybory));
            model.addAttribute("kandydatPrezydent", kandydatPrezydentRepository.findOne(idKandydatPrezydent));
        }
        return "main";
    }

    @RequestMapping(value = "/wybory/szczegoly/prezydenckie/edycja", method = RequestMethod.POST)
    public String szczegolyPrezydenckieEdycja(@RequestParam(value = "idWybory") int idWybory, @RequestParam(value = "idKandydatPrezydent") int idKandydatPrezydent, @Valid KandydatPrezydent kandydatPrezydent, BindingResult bindingResult, Model model) {
        model.addAttribute("view", "wybory/szczegoly/edycja");
        model.addAttribute("edit", true);
        model.addAttribute("exists", false);
        model.addAttribute("idWybory", idWybory);
        model.addAttribute("idKandydatPrezydent", idKandydatPrezydent);
        if (wyboryRepository.exists(idWybory) && kandydatPrezydentRepository.exists(idKandydatPrezydent)) {
            model.addAttribute("exists", true);
            model.addAttribute("wybory", wyboryRepository.findOne(idWybory));
            KandydatPrezydent kandydatPrezydentDoEdycji = kandydatPrezydentRepository.findOne(idKandydatPrezydent);
            if (!bindingResult.hasErrors()) {
                if (kandydatPrezydentRepository.kandydatPrezydentExists(idWybory, kandydatPrezydent.getNrNaLiscie()) && kandydatPrezydent.getNrNaLiscie() != kandydatPrezydentDoEdycji.getNrNaLiscie()) {
                    bindingResult.rejectValue("nrNaLiscie", "error.nrNaLiscie.exists", "Ten numer na liście jest już zajęty wybierz inny.");
                } else {
                    kandydatPrezydent.setId(kandydatPrezydentDoEdycji.getId());
                    kandydatPrezydent.setWybory(wyboryRepository.findOne(idWybory));
                    kandydatPrezydentRepository.save(kandydatPrezydent);
                    model.addAttribute("view", null);
                    model.addAttribute("edit", null);
                    model.addAttribute("exists", null);
                    model.addAttribute("wybory", null);
                    model.addAttribute("idWybory", null);
                    model.addAttribute("idKandydatPrezydent", null);
                    model.addAttribute("idWybory", idWybory);
                    model.addAttribute("success", true);
                    return "redirect:/wybory/szczegoly";
                }
            }
        }
        return "main";
    }

    @RequestMapping(value = "/wybory/szczegoly/prezydenckie/usun")
    public String szczegolyPrezydenckieUsun(@RequestParam(value = "idWybory") int idWybory, @RequestParam(value = "idKandydatPrezydent") int idKandydatPrezydent, Model model) {
        model.addAttribute("view", "wybory/szczegoly/index");
        model.addAttribute("exists", false);
        model.addAttribute("success", false);
        if (kandydatPrezydentRepository.exists(idKandydatPrezydent)) {
            model.addAttribute("exists", true);
            kandydatPrezydentRepository.delete(idKandydatPrezydent);
            model.addAttribute("idWybory", idWybory);
            model.addAttribute("success", true);
            model.addAttribute("wybory", wyboryRepository.findOne(idWybory));
        } else {
            model.addAttribute("error", "Wybrane wybory nie istnieją.");
        }
        return "main";
    }
}
