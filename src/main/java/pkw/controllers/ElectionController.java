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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
        return wyboryRepository.findAll();
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
            model.addAttribute("success", true);
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Uzytkownik loggedUser = uzytkownikRepository.findByLogin(user.getUsername()).get(0);
            wybory.setTworca(loggedUser);
            wybory.setDataUtworzenia(new LocalDate());
            wybory.setTypWyborow(typWyborowRepository.findOne(wybory.getTypWyborowId()));
            wyboryRepository.save(wybory);
            return "main";
        }
    }
}
