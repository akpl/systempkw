package pkw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pkw.models.*;

import javax.validation.Valid;

@Controller
public class UserController {
    @Autowired
    private UzytkownikRepository uzytkownikRepository;

    @Autowired
    private PoziomDostepuRepository poziomDostepuRepository;

    @ModelAttribute("users")
    public Iterable<Uzytkownik> users() {
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
    public String userBrowse(Model model) {
        model.addAttribute("view", "user/browse");
        return "main";
    }

    @RequestMapping(value = "/user/add")
    public String userAdd(Model model) {
        model.addAttribute("view", "user/add");
        return "main";
    }

    @RequestMapping(value = "/user/add", method = RequestMethod.POST)
    public String add(@Valid Uzytkownik user, BindingResult bindingResult, Model model) {
        model.addAttribute("view", "user/add");
        if (bindingResult.hasErrors()) {
            return "main";
        } else {
            model.addAttribute("success", true);
            ShaPasswordEncoder sha = new ShaPasswordEncoder(256);
            String encodedPassword = sha.encodePassword(user.getHaslo(), null);
            user.setHaslo(encodedPassword);
            PoziomDostepu poziomDostepu = poziomDostepuRepository.findOne(user.getPoziomDostepuId());
            user.setPoziomDostepu(poziomDostepu);
            uzytkownikRepository.save(user);
            return "main";
        }
    }
}
