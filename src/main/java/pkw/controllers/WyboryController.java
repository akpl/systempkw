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
public class WyboryController {
    @Autowired
    private WyboryRepository wyboryRepository;

    @Autowired
    private UzytkownikRepository uzytkownikRepository;

    @Autowired
    private TypWyborowRepository typWyborowRepository;

    @ModelAttribute("wyboryList")
    public Iterable<Wybory> wyboryList() {
        return wyboryRepository.findByOrderByIdAsc();
    }

    @RequestMapping(value = "/wybory")
    public String index(Model model) {
        model.addAttribute("view", "wybory/index");
        return "main";
    }

    @RequestMapping(value = "/wybory/dodaj", method = RequestMethod.GET)
    public String dodaj(Wybory wybory, Model model) {
        model.addAttribute("view", "wybory/dodaj");
        return "main";
    }

    @RequestMapping(value = "/wybory/dodaj", method = RequestMethod.POST)
    public String dodaj(@Valid Wybory wybory, BindingResult bindingResult, Model model) {
        model.addAttribute("view", "wybory/dodaj");
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
            model.addAttribute("view", "wybory/zapisano");
            return "main";
        }
    }

    @RequestMapping(value = "/wybory/edycja", method = RequestMethod.GET)
    public String edycja(@RequestParam(value = "id") int id, Model model) {
        model.addAttribute("view", "wybory/edycja");
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

    @RequestMapping(value = "/wybory/edycja", method = RequestMethod.POST)
    public String edycja(@RequestParam(value = "id") int id, @Valid Wybory wybory, BindingResult bindingResult, Model model) {
        model.addAttribute("view", "wybory/edycja");
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
                model.addAttribute("view", "wybory/zapisano");
            }
        }
        return "main";
    }

    @RequestMapping(value = "/wybory/usun")
    public String usun(@RequestParam(value = "id") int id, Model model) {
        model.addAttribute("view", "wybory/usun");
        model.addAttribute("exists", false);
        if (wyboryRepository.exists(id)) {
            model.addAttribute("exists", true);
            Wybory wyboryDoUsuniecia = wyboryRepository.findOne(id);
            model.addAttribute("wybory", wyboryDoUsuniecia);
        }
        return "main";
    }

    @RequestMapping(value = "/wybory/usunieto")
    public String usunieto(@RequestParam(value = "id") int id, Model model) {
        model.addAttribute("view", "wybory/usunieto");
        model.addAttribute("success", false);
        if (wyboryRepository.exists(id)) {
            wyboryRepository.delete(id);
            model.addAttribute("success", true);
        }
        return "main";
    }

    @RequestMapping(value = "/wybory/szczegoly")
    public String szczegoly(@RequestParam(value = "idWybory") int idWybory, @RequestParam(value = "success", required = false, defaultValue = "false") boolean success, Model model) {
        model.addAttribute("view", "wybory/szczegoly/index");
        model.addAttribute("exists", false);
        model.addAttribute("success", success);
        if (wyboryRepository.exists(idWybory)) {
            model.addAttribute("exists", true);
            model.addAttribute("wybory", wyboryRepository.findOne(idWybory));
        }
        return "main";
    }
}