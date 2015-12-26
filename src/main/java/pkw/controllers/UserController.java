package pkw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pkw.models.*;

import javax.validation.Valid;

@Controller
public class UserController {
    @Autowired
    private UzytkownikRepository uzytkownikRepository;

    @Autowired
    private PoziomDostepuRepository poziomDostepuRepository;

    @ModelAttribute("uzytkownicyList")
    public Iterable<Uzytkownik> uzytkownicyList() {
        return uzytkownikRepository.findAll();
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginForm() {
        return "login";
    }

    @RequestMapping(value = "/logout-success", method = RequestMethod.GET)
    public String logoutSuccess() {
        return "logout-success";
    }

    @RequestMapping(value = "/user/browse")
    public String browse(Model model) {
        model.addAttribute("view", "user/browse");
        return "main";
    }

    @RequestMapping(value = "/user/add", method = RequestMethod.GET)
    public String add(Uzytkownik uzytkownik, Model model) {
        model.addAttribute("view", "user/add");
        return "main";
    }

    @RequestMapping(value = "/user/add", method = RequestMethod.POST)
    public String add(@Valid Uzytkownik uzytkownik, BindingResult bindingResult, Model model) {
        model.addAttribute("view", "user/add");
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
            model.addAttribute("view", "user/saved");
            return "main";
        }
    }

    @RequestMapping(value = "/user/edit")
    public String edit(@RequestParam(value = "id") int id, Model model) {
        model.addAttribute("view", "user/edit");
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

    @RequestMapping(value = "/user/edit", method = RequestMethod.POST)
    public String edit(@RequestParam(value = "id") int id, @Valid Uzytkownik uzytkownik, BindingResult bindingResult, Model model) {
        model.addAttribute("view", "user/edit");
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
                    PoziomDostepu poziomDostepu = poziomDostepuRepository.findOne(uzytkownik.getPoziomDostepuId());
                    uzytkownik.setPoziomDostepu(poziomDostepu);
                    uzytkownikRepository.save(uzytkownik);
                    model.addAttribute("view", "user/saved");
                }
            }
        }
        return "main";
    }

    @RequestMapping(value = "/user/delete")
    public String delete(@RequestParam(value = "id") int id, Model model) {
        model.addAttribute("view", "user/delete");
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

    @RequestMapping(value = "/user/delete-confirm")
    public String deleteConfirm(@RequestParam(value = "id") int id, Model model) {
        model.addAttribute("view", "user/delete-confirm");
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
}
