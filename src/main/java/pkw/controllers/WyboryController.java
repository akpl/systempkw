package pkw.controllers;

import org.joda.time.LocalDate;
import org.joda.time.Months;
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
import pkw.models.TypWyborow;
import pkw.models.Uzytkownik;
import pkw.models.Wybory;
import pkw.models.Newsletter;
import pkw.repositories.TypWyborowRepository;
import pkw.repositories.UzytkownikRepository;
import pkw.repositories.WyboryRepository;
import pkw.repositories.NewsletterRepository;
import pkw.SendMail;

import javax.validation.Valid;
import java.lang.Iterable;

@Controller
@RequestMapping("/panel")
public class WyboryController {
    @Autowired
    private WyboryRepository wyboryRepository;

    @Autowired
    private UzytkownikRepository uzytkownikRepository;

    @Autowired
    private TypWyborowRepository typWyborowRepository;

    @Autowired
    private NewsletterRepository newsletterRepository;

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
            Iterable<Newsletter> listNewsletter = newsletterRepository.findAll();
            SendMail news = new SendMail();
            for(Newsletter a : listNewsletter)
            {
                news.addRecipientBccMail(a.getEmail());
            }
            StringBuilder content = new StringBuilder("Zostały ogłoszone wybory.</br>");
            content.append("Odbędą się one ");
            content.append(wybory.getDataGlosowania());
            content.append(" czyli za ok. ");
            content.append(Months.monthsBetween(wybory.getDataUtworzenia(),wybory.getDataGlosowania()).getMonths());
            content.append(" miesięcy.</br>");
            news.setSubject("[PKW] Ogłoszenie o nowych wyborach");
            news.setContent(content.toString());
            news.sendEmail();
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
                Iterable<Newsletter> listNewsletter = newsletterRepository.findAll();
                SendMail news = new SendMail();
                for(Newsletter a : listNewsletter)
                {
                    news.addRecipientBccMail(a.getEmail());
                }
                StringBuilder content = new StringBuilder("Zostały zmienione wybory.</br>");
                content.append("Odbędą się one ");
                content.append(wybory.getDataGlosowania());
                content.append(" czyli za ok. ");
                content.append(Months.monthsBetween(wybory.getDataUtworzenia(),wybory.getDataGlosowania()).getMonths());
                content.append(" miesięcy.</br>");
                news.setSubject("[PKW] Zmiana wyborów");
                news.setContent(content.toString());
                news.sendEmail();
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
