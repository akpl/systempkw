package pkw.controllers;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.ModelAndView;
import pkw.models.*;

import javax.validation.Valid;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class ElectionController {
    @Autowired
    private WyboryRepository wyboryRepository;

    @Autowired
    private UzytkownikRepository uzytkownikRepository;

    @Autowired
    private TypWyborowRepository typWyborowRepository;

    @Autowired
    private PytanieReferendalneRepository pytanieReferendalneRepository;

    @ModelAttribute("wyboryList")
    public Iterable<Wybory> wyboryList() {
        return wyboryRepository.findByOrderByIdAsc();
    }

    @RequestMapping(value = "/election/browse")
    public String browse(Model model) {
        model.addAttribute("view", "election/browse");
        return "main";
    }

    @RequestMapping(value = "/election/add", method = RequestMethod.GET)
    public String add(Wybory wybory, Model model) {
        model.addAttribute("view", "election/add");
        return "main";
    }

    @RequestMapping(value = "/election/add", method = RequestMethod.POST)
    public String add(@Valid Wybory wybory, BindingResult bindingResult, Model model) {
        model.addAttribute("view", "election/add");
        if (!bindingResult.hasErrors()) {
            LocalDate currentDate = new LocalDate();
            if (wybory.getDataGlosowania().isBefore(currentDate)) {
                bindingResult.rejectValue("dataGlosowania", "error.dataGlosowania.past", "Nie mozesz utworzyć wyborów w przeszłości");
            }
        }
        if (bindingResult.hasErrors()) {
            return "main";
        } else {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Uzytkownik loggedUser = uzytkownikRepository.findByLogin(user.getUsername()).get(0);
            wybory.setTworca(loggedUser);
            wybory.setDataUtworzenia(new LocalDate());
            wybory.setTypWyborow(typWyborowRepository.findOne(wybory.getTypWyborowId()));
            wyboryRepository.save(wybory);
            model.addAttribute("view", "election/saved");
            return "main";
        }
    }

    @RequestMapping(value = "/election/edit", method = RequestMethod.GET)
    public String edit(@RequestParam(value = "id") int id, Model model) {
        model.addAttribute("view", "election/edit");
        model.addAttribute("edit", true);
        model.addAttribute("exists", false);
        model.addAttribute("id", id);
        if (wyboryRepository.exists(id)) {
            model.addAttribute("exists", true);
            Wybory wyboryDoEdycji = wyboryRepository.findOne(id);
            wyboryDoEdycji.setTypWyborowId(wyboryDoEdycji.getTypWyborow().getId());
            model.addAttribute("wybory", wyboryDoEdycji);
        }
        return "main";
    }

    @RequestMapping(value = "/election/edit", method = RequestMethod.POST)
    public String edit(@RequestParam(value = "id") int id, @Valid Wybory wybory, BindingResult bindingResult, Model model) {
        model.addAttribute("view", "election/edit");
        model.addAttribute("edit", true);
        model.addAttribute("exists", false);
        model.addAttribute("id", id);
        if (wyboryRepository.exists(id)) {
            model.addAttribute("exists", true);
            Wybory wyboryDoEdycji = wyboryRepository.findOne(id);
            if (!bindingResult.hasErrors()) {
                wybory.setId(id);
                wybory.setTworca(wyboryDoEdycji.getTworca());
                wybory.setDataUtworzenia(wyboryDoEdycji.getDataUtworzenia());
                TypWyborow typWyborow = typWyborowRepository.findOne(wybory.getTypWyborowId());
                wybory.setTypWyborow(typWyborow);
                wyboryRepository.save(wybory);
                model.addAttribute("view", "election/saved");
            }
        }
        return "main";
    }

    @RequestMapping(value = "/election/delete")
    public String delete(@RequestParam(value = "id") int id, Model model) {
        model.addAttribute("view", "election/delete");
        model.addAttribute("exists", false);
        if (wyboryRepository.exists(id)) {
            model.addAttribute("exists", true);
            Wybory wyboryDoUsuniecia = wyboryRepository.findOne(id);
            model.addAttribute("wybory", wyboryDoUsuniecia);
        }
        return "main";
    }

    @RequestMapping(value = "/election/delete-confirm")
    public String deleteConfirm(@RequestParam(value = "id") int id, Model model) {
        model.addAttribute("view", "election/delete-confirm");
        model.addAttribute("success", false);
        if (wyboryRepository.exists(id)) {
            wyboryRepository.delete(id);
            model.addAttribute("success", true);
        }
        return "main";
    }

    @RequestMapping(value = "/election/szczegoly")
    public String szczegoly(@RequestParam(value = "idWybory") int idWybory, @RequestParam(value = "success", required = false, defaultValue = "false") boolean success, Model model) {
        model.addAttribute("view", "election/szczegoly/index");
        model.addAttribute("exists", false);
        model.addAttribute("success", success);
        if (wyboryRepository.exists(idWybory)) {
            model.addAttribute("exists", true);
            model.addAttribute("wybory", wyboryRepository.findOne(idWybory));
        }
        return "main";
    }

    @RequestMapping(value = "/election/szczegoly/dodaj", method = RequestMethod.GET)
    public String szczegolyDodaj(@RequestParam(value = "idWybory") int idWybory, PytanieReferendalne pytanieReferendalne, Model model) {
        model.addAttribute("view", "election/szczegoly/dodaj");
        model.addAttribute("exists", false);
        model.addAttribute("idWybory", idWybory);
        if (wyboryRepository.exists(idWybory)) {
            model.addAttribute("exists", true);
            model.addAttribute("wybory", wyboryRepository.findOne(idWybory));
        }
        return "main";
    }

    @RequestMapping(value = "/election/szczegoly/dodaj", method = RequestMethod.POST)
    public String szczegolyDodaj(@RequestParam(value = "idWybory") int idWybory, @Valid PytanieReferendalne pytanieReferendalne, BindingResult bindingResult, Model model) {
        model.addAttribute("view", "election/szczegoly/dodaj");
        model.addAttribute("exists", false);
        model.addAttribute("idWybory", idWybory);
        if (wyboryRepository.exists(idWybory)) {
            model.addAttribute("exists", true);
            model.addAttribute("wybory", wyboryRepository.findOne(idWybory));
            if (!bindingResult.hasErrors()) {
                pytanieReferendalne.setWybory(wyboryRepository.findOne(idWybory));
                pytanieReferendalneRepository.save(pytanieReferendalne);
                //model.addAttribute("view", "election/szczegoly/index");
                //model.addAttribute("success", true);
                model.addAttribute("view", null);
                model.addAttribute("exists", null);
                model.addAttribute("wybory", null);
                model.addAttribute("success", true);
                return "redirect:/election/szczegoly";
            }
        }
        return "main";
    }

    @RequestMapping(value = "/election/szczegoly/edycja", method = RequestMethod.GET)
    public String szczegolyEdycja(@RequestParam(value = "idWybory") int idWybory, @RequestParam(value = "idPytanieReferendalne") int idPytanieReferendalne, Model model) {
        model.addAttribute("view", "election/szczegoly/edycja");
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

    @RequestMapping(value = "/election/szczegoly/edycja", method = RequestMethod.POST)
    public String szczegolyEdycja(@RequestParam(value = "idWybory") int idWybory, @RequestParam(value = "idPytanieReferendalne") int idPytanieReferendalne, @Valid PytanieReferendalne pytanieReferendalne, BindingResult bindingResult, Model model) {
        model.addAttribute("view", "election/szczegoly/edycja");
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
                return "redirect:/election/szczegoly";
            }
        }
        return "main";
    }

    @RequestMapping(value = "/election/szczegoly/usun")
    public String szczegolyUsun(@RequestParam(value = "idWybory") int idWybory, @RequestParam(value = "idPytanieReferendalne") int idPytanieReferendalne, Model model) {
        model.addAttribute("view", "election/szczegoly/index");
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
