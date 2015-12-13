package pkw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pkw.models.User;
import pkw.models.UserDAO;

@Controller
public class UserController {
    @Autowired
    JdbcTemplate jdbcTemplate;
    private UserDAO userDAO = new UserDAO(jdbcTemplate);

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginForm(Model model) {
        //model.addAttribute("invalidLogin", true);
        return "login";
    }
/*
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(Model model, @RequestParam("username") String username, @RequestParam("password") String password) {
        User user = userDAO.getByLogin(username);
        if(user == null || !user.checkPassword(password))
            model.addAttribute("invalidLogin", true);
        return "login";
    }*/
}
