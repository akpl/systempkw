package pkw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pkw.models.PoziomDostepu;
import pkw.models.Newsletter;
import pkw.repositories.NewsletterRepository;

import javax.validation.Valid;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {


    @Autowired
    private NewsletterRepository newsletterRepository;

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("view", "home");
        return "main";
    }
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginForm() {
        return "login";
    }

    @RequestMapping(value = "/logout-success", method = RequestMethod.GET)
    public String logoutSuccess() {
        return "logout-success";
    }

    @RequestMapping(value = "/newsletter", method = RequestMethod.POST)
    public String signNewsletter(@Valid Newsletter newsletter, BindingResult bindingResult, Model model) {
        model.addAttribute("view", "newsletter");
        if (!bindingResult.hasErrors()) {
        }
        if (bindingResult.hasErrors()) {
            return "main";
        } else {
            newsletter.setEmail(newsletter.getEmail());
            newsletterRepository.save(newsletter);
            return "public/index";
        }
    }
}