package pkw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pkw.models.Election;
import pkw.models.ElectionDAO;
import pkw.models.User;
import pkw.models.UserDAO;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserDAO userDAO;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginForm() {
        return "login";
    }

    @RequestMapping(value = "/logout-success", method = RequestMethod.GET)
    public String logoutSuccess() {
        return "logout-success";
    }

    @ModelAttribute("users")
    public List<User> users() {
        return userDAO.selectAll();
    }

    @RequestMapping(value = "/user/browse")
    public String electionBrowse(Model model) {
        model.addAttribute("view", "user/browse");
        return "main";
    }

    @RequestMapping(value = "/user/add")
    public String electionAdd(Model model) {
        model.addAttribute("view", "user/add");
        return "main";
    }

    @RequestMapping(value = "/user/add", method = RequestMethod.POST)
    public String add(@Valid User user, BindingResult bindingResult, Model model) {
        model.addAttribute("view", "user/add");
        if (bindingResult.hasErrors()) {
            return "main";
        } else {
            model.addAttribute("success", true);
            ShaPasswordEncoder sha = new ShaPasswordEncoder(256);
            String encodedPassword = sha.encodePassword(user.getPassword(), null);
            user.setPassword(encodedPassword);
            userDAO.insert(user);
            return "main";
        }
    }
}
