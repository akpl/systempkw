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
}
