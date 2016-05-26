package pkw.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pkw.Counter;
import pkw.SendMail;
import pkw.models.Logowanie;
import pkw.models.PoziomDostepu;
import pkw.models.Uzytkownik;
import pkw.repositories.LogowanieRepository;
import pkw.repositories.PoziomDostepuRepository;
import pkw.repositories.UzytkownikRepository;

import javax.validation.Valid;
import java.math.BigInteger;
import java.security.SecureRandom;

@Controller
@RequestMapping("/panel")
public class UzytkownikController {
    @Autowired
    private UzytkownikRepository uzytkownikRepository;

    @Autowired
    private PoziomDostepuRepository poziomDostepuRepository;

    @Autowired
    private LogowanieRepository logowanieRepository;

    private static final Logger logger = LoggerFactory.getLogger(UzytkownikController.class);

    @ModelAttribute("uzytkownicyList")
    public Iterable<Uzytkownik> uzytkownicyList() {
        return uzytkownikRepository.findByOrderByIdAsc();
    }

    @ModelAttribute("logowaniaList")
    public Iterable<Logowanie> logowaniaList() {
        return logowanieRepository.findByOrderByIdDesc();
    }

    @RequestMapping(value = "/uzytkownik")
    public String index(Model model) {
        model.addAttribute("view", "uzytkownik/index");
        return "main";
    }

    @RequestMapping(value = "/uzytkownik/dodaj", method = RequestMethod.GET)
    public String dodaj(Uzytkownik uzytkownik, Model model) {
        model.addAttribute("view", "uzytkownik/dodaj");
        return "main";
    }

    @RequestMapping(value = "/uzytkownik/dodaj", method = RequestMethod.POST)
    public String dodaj(@Valid Uzytkownik uzytkownik, BindingResult bindingResult, Model model) {
        model.addAttribute("view", "uzytkownik/dodaj");
        if (!bindingResult.hasErrors()) {
            if (uzytkownikRepository.findByLogin(uzytkownik.getLogin()).size() > 0) {
                bindingResult.rejectValue("login", "error.login.exists", "Uzytkownik o takim loginie juz istnieje");
            }
        }
        if (bindingResult.hasErrors()) {
            return "main";
        } else {
            ShaPasswordEncoder sha = new ShaPasswordEncoder(256);
            String encodedPassword = sha.encodePassword(uzytkownik.getHaslo(), null);
            uzytkownik.setHaslo(encodedPassword);
            PoziomDostepu poziomDostepu = poziomDostepuRepository.findOne(uzytkownik.getPoziomDostepuId());
            uzytkownik.setPoziomDostepu(poziomDostepu);
            uzytkownikRepository.save(uzytkownik);
            model.addAttribute("view", "uzytkownik/zapisano");
            return "main";
        }
    }

    @RequestMapping(value = "/uzytkownik/edycja", method = RequestMethod.GET)
    public String edycja(@RequestParam(value = "id") int id, Model model) {
        model.addAttribute("view", "uzytkownik/edycja");
        model.addAttribute("edit", true);
        model.addAttribute("exists", false);
        model.addAttribute("currentUser", false);
        if (uzytkownikRepository.exists(id)) {
            model.addAttribute("exists", true);
            Uzytkownik uzytkownikDoEdycji = uzytkownikRepository.findOne(id);
            uzytkownikDoEdycji.setPoziomDostepuId(uzytkownikDoEdycji.getPoziomDostepu().getId());
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Uzytkownik loggedUser = uzytkownikRepository.findByLogin(user.getUsername()).get(0);
            if (loggedUser.getId() == uzytkownikDoEdycji.getId()) {
                model.addAttribute("currentUser", true);
            }
            else {
                model.addAttribute("uzytkownik", uzytkownikDoEdycji);
            }
        }
        return "main";
    }

    @RequestMapping(value = "/uzytkownik/edycja", method = RequestMethod.POST)
    public String edycja(@RequestParam(value = "id") int id, @Valid Uzytkownik uzytkownik, BindingResult bindingResult, Model model) {
        model.addAttribute("view", "uzytkownik/edycja");
        model.addAttribute("edit", true);
        model.addAttribute("exists", false);
        model.addAttribute("currentUser", false);
        if (uzytkownikRepository.exists(id)) {
            model.addAttribute("exists", true);
            Uzytkownik uzytkownikDoEdycji = uzytkownikRepository.findOne(id);
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Uzytkownik loggedUser = uzytkownikRepository.findByLogin(user.getUsername()).get(0);
            if (loggedUser.getId() == uzytkownikDoEdycji.getId()) {
                model.addAttribute("currentUser", true);
            }
            else {
                if (!bindingResult.hasErrors()) {
                    uzytkownik.setId(id);
                    uzytkownik.setHaslo(uzytkownikDoEdycji.getHaslo());
                    PoziomDostepu poziomDostepu = poziomDostepuRepository.findOne(uzytkownik.getPoziomDostepuId());
                    uzytkownik.setPoziomDostepu(poziomDostepu);
                    uzytkownikRepository.save(uzytkownik);
                    model.addAttribute("view", "uzytkownik/zapisano");
                }
            }
        }
        return "main";
    }

    @RequestMapping(value = "/uzytkownik/usun")
    public String usun(@RequestParam(value = "id") int id, Model model) {
        model.addAttribute("view", "uzytkownik/usun");
        model.addAttribute("exists", false);
        model.addAttribute("currentUser", false);
        if (uzytkownikRepository.exists(id)) {
            model.addAttribute("exists", true);
            Uzytkownik uzytkownikDoUsuniecia = uzytkownikRepository.findOne(id);
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Uzytkownik loggedUser = uzytkownikRepository.findByLogin(user.getUsername()).get(0);
            if (loggedUser.getId() == uzytkownikDoUsuniecia.getId()) {
                model.addAttribute("currentUser", true);
            }
            else {
                model.addAttribute("uzytkownik", uzytkownikDoUsuniecia);
            }
        }
        return "main";
    }

    @RequestMapping(value = "/uzytkownik/usunieto")
    public String usunieto(@RequestParam(value = "id") int id, Model model) {
        model.addAttribute("view", "uzytkownik/usunieto");
        model.addAttribute("success", false);
        model.addAttribute("currentUser", false);
        if (uzytkownikRepository.exists(id)) {
            Uzytkownik uzytkownikDoUsuniecia = uzytkownikRepository.findOne(id);
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Uzytkownik loggedUser = uzytkownikRepository.findByLogin(user.getUsername()).get(0);
            if (loggedUser.getId() == uzytkownikDoUsuniecia.getId()) {
                model.addAttribute("currentUser", true);
            }
            else {
                uzytkownikRepository.delete(id);
                model.addAttribute("success", true);
            }
        }
        return "main";
    }

    @RequestMapping(value = "/uzytkownik/historia")
    public String historia(Model model) {
        model.addAttribute("view", "uzytkownik/historia_logowan");
        model.addAttribute("counter", new Counter());
        return "main";
    }

    @RequestMapping(value = "/uzytkownik/historia/{idUzytkownika}")
    public String historiaUzytkownika(@PathVariable int idUzytkownika, Model model) {
        model.addAttribute("view", "uzytkownik/historia_logowan_uzytkownika");
        if (uzytkownikRepository.exists(idUzytkownika)) {
            Uzytkownik uzytkownik = uzytkownikRepository.findOne(idUzytkownika);
            model.addAttribute("uzytkownik", uzytkownik);
            model.addAttribute("counter", new Counter());
        }
        return "main";
    }

    @RequestMapping(value = "/uzytkownik/haslo")
    public String haslo(@RequestParam(value = "id") int id, Model model) {
        model.addAttribute("view", "uzytkownik/haslo");
        if (uzytkownikRepository.exists(id)) {
            model.addAttribute("exists", true);
            model.addAttribute("uzytkownik", uzytkownikRepository.findOne(id));
        }
        return "main";
    }

    @RequestMapping(value = "/uzytkownik/zresetowano")
    public String zresetowano(@RequestParam(value = "id") int id, Model model) {
        model.addAttribute("view", "uzytkownik/zresetowano");
        if (uzytkownikRepository.exists(id)) {
            model.addAttribute("exists", true);
            Uzytkownik uzytkownik = uzytkownikRepository.findOne(id);
            if (uzytkownik.getEmail() != null) {
                try {
                    SecureRandom random = new SecureRandom();
                    String newPassword = new BigInteger(60, random).toString(32);
                    SendMail mail = new SendMail();
                    String login = uzytkownik.getLogin();
                    mail.setContent("Nowe hasło użytkownika " + login + " to:<br />" + newPassword);
                    mail.addRecipientToMail(uzytkownik.getEmail());
                    mail.setSubject("[PKW] Reset hasła użytkownika " + login);
                    mail.sendEmail();
                    ShaPasswordEncoder sha = new ShaPasswordEncoder(256);
                    String encodedPassword = sha.encodePassword(newPassword, null);
                    uzytkownik.setHaslo(encodedPassword);
                    uzytkownikRepository.save(uzytkownik);
                    model.addAttribute("success", true);
                } catch (Exception e) {
                    model.addAttribute("success", false);
                    logger.error("Error during sending email", e);
                }
            } else model.addAttribute("success", false);
        }
        return "main";
    }

    @RequestMapping("/uzytkownik/zablokuj")
    public String zablokuj(@RequestParam(value = "id") int id, Model model, @AuthenticationPrincipal UserDetails loggedUser) {
        model.addAttribute("view", "uzytkownik/index");
        Uzytkownik uzytkownik = uzytkownikRepository.findOne(id);
        model.addAttribute("zablokuj", false);
        if (!loggedUser.getUsername().equals(uzytkownik.getLogin())) {
            if (uzytkownik.isAktywny()) {
                uzytkownik.setAktywny(false);
                uzytkownikRepository.save(uzytkownik);
                model.addAttribute("zablokuj", true);
            }
        }
        return "main";
    }

    @RequestMapping("/uzytkownik/odblokuj")
    public String odblokuj(@RequestParam(value = "id") int id, Model model) {
        model.addAttribute("view", "uzytkownik/index");
        Uzytkownik uzytkownik = uzytkownikRepository.findOne(id);
        model.addAttribute("odblokuj", false);
        if (!uzytkownik.isAktywny()) {
            uzytkownik.setAktywny(true);
            uzytkownikRepository.save(uzytkownik);
            model.addAttribute("odblokuj", true);
        }
        return "main";
    }
}
