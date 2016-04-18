package pkw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pkw.models.PytanieReferendalne;
import pkw.repositories.PytanieReferendalneRepository;
import pkw.repositories.WyboryRepository;

import javax.validation.Valid;

@Controller
@RequestMapping("/panel")
public class ReferendumController {
    @Autowired
    private WyboryRepository wyboryRepository;

    @Autowired
    private PytanieReferendalneRepository pytanieReferendalneRepository;

    @RequestMapping(value = "/wybory/szczegoly/referendum/dodaj", method = RequestMethod.GET)
    public String szczegolyReferendumDodaj(@RequestParam(value = "idWybory") int idWybory, PytanieReferendalne pytanieReferendalne, Model model) {
        model.addAttribute("view", "wybory/szczegoly/dodaj");
        model.addAttribute("exists", false);
        model.addAttribute("idWybory", idWybory);
        if (wyboryRepository.exists(idWybory)) {
            model.addAttribute("exists", true);
            model.addAttribute("wybory", wyboryRepository.findOne(idWybory));
        }
        return "main";
    }

    @RequestMapping(value = "/wybory/szczegoly/referendum/dodaj", method = RequestMethod.POST)
    public String szczegolyReferendumDodaj(@RequestParam(value = "idWybory") int idWybory, @Valid PytanieReferendalne pytanieReferendalne, BindingResult bindingResult, Model model) {
        model.addAttribute("view", "wybory/szczegoly/dodaj");
        model.addAttribute("exists", false);
        model.addAttribute("idWybory", idWybory);
        if (wyboryRepository.exists(idWybory)) {
            model.addAttribute("exists", true);
            model.addAttribute("wybory", wyboryRepository.findOne(idWybory));
            if (!bindingResult.hasErrors()) {
                pytanieReferendalne.setWybory(wyboryRepository.findOne(idWybory));
                pytanieReferendalneRepository.save(pytanieReferendalne);
                //model.addAttribute("view", "wybory/szczegoly/index");
                //model.addAttribute("success", true);
                model.addAttribute("view", null);
                model.addAttribute("exists", null);
                model.addAttribute("wybory", null);
                model.addAttribute("success", true);
                return "redirect:/wybory/szczegoly";
            }
        }
        return "main";
    }

    @RequestMapping(value = "/wybory/szczegoly/referendum/edycja", method = RequestMethod.GET)
    public String szczegolyReferendumEdycja(@RequestParam(value = "idWybory") int idWybory, @RequestParam(value = "idPytanieReferendalne") int idPytanieReferendalne, Model model) {
        model.addAttribute("view", "wybory/szczegoly/edycja");
        model.addAttribute("edit", true);
        model.addAttribute("exists", false);
        model.addAttribute("idWybory", idWybory);
        model.addAttribute("idPytanieReferendalne", idPytanieReferendalne);
        if (wyboryRepository.exists(idWybory) && pytanieReferendalneRepository.exists(idPytanieReferendalne)) {
            model.addAttribute("exists", true);
            model.addAttribute("wybory", wyboryRepository.findOne(idWybory));
            model.addAttribute("pytanieReferendalne", pytanieReferendalneRepository.findOne(idPytanieReferendalne));
        }
        return "main";
    }

    @RequestMapping(value = "/wybory/szczegoly/referendum/edycja", method = RequestMethod.POST)
    public String szczegolyReferendumEdycja(@RequestParam(value = "idWybory") int idWybory, @RequestParam(value = "idPytanieReferendalne") int idPytanieReferendalne, @Valid PytanieReferendalne pytanieReferendalne, BindingResult bindingResult, Model model) {
        model.addAttribute("view", "wybory/szczegoly/edycja");
        model.addAttribute("edit", true);
        model.addAttribute("exists", false);
        model.addAttribute("idWybory", idWybory);
        model.addAttribute("idPytanieReferendalne", idPytanieReferendalne);
        if (wyboryRepository.exists(idWybory) && pytanieReferendalneRepository.exists(idPytanieReferendalne)) {
            model.addAttribute("exists", true);
            model.addAttribute("wybory", wyboryRepository.findOne(idWybory));
            if (!bindingResult.hasErrors()) {
                PytanieReferendalne pytanieReferendalneDoEdycji = pytanieReferendalneRepository.findOne(idPytanieReferendalne);
                pytanieReferendalne.setId(pytanieReferendalneDoEdycji.getId());
                pytanieReferendalne.setWybory(wyboryRepository.findOne(idWybory));
                pytanieReferendalneRepository.save(pytanieReferendalne);
                model.addAttribute("view", null);
                model.addAttribute("edit", null);
                model.addAttribute("exists", null);
                model.addAttribute("wybory", null);
                model.addAttribute("idWybory", null);
                model.addAttribute("idPytanieReferendalne", null);
                model.addAttribute("idWybory", idWybory);
                model.addAttribute("success", true);
                return "redirect:/wybory/szczegoly";
            }
        }
        return "main";
    }

    @RequestMapping(value = "/wybory/szczegoly/referendum/usun")
    public String szczegolyReferendumUsun(@RequestParam(value = "idWybory") int idWybory, @RequestParam(value = "idPytanieReferendalne") int idPytanieReferendalne, Model model) {
        model.addAttribute("view", "wybory/szczegoly/index");
        model.addAttribute("exists", false);
        model.addAttribute("success", false);
        if (pytanieReferendalneRepository.exists(idPytanieReferendalne)) {
            model.addAttribute("exists", true);
            pytanieReferendalneRepository.delete(idPytanieReferendalne);
            model.addAttribute("idWybory", idWybory);
            model.addAttribute("success", true);
            model.addAttribute("wybory", wyboryRepository.findOne(idWybory));
        } else {
            model.addAttribute("error", "Wybrane wybory nie istnieją.");
        }
        return "main";
    }
}
