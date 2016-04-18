package pkw.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {

    @RequestMapping("/")
    public String index() {
        return "public/index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginForm() {
        return "login";
    }
}