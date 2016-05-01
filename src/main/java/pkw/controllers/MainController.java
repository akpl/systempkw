package pkw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pkw.ImageNotFoundException;
import pkw.models.Newsletter;
import pkw.repositories.NewsletterRepository;

import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Controller
public class MainController {


    @Autowired
    private NewsletterRepository newsletterRepository;

    @RequestMapping("/")
    public String index(Newsletter newsletter) {
        return "public/index";
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

    @RequestMapping(value = "/upload/prezydent/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getPrezydentImage(@PathVariable int id) throws FileNotFoundException {
        String path = "upload/prezydent/" + id + ".jpg";
        File file = new File(path);
        if (!file.exists()) throw new ImageNotFoundException();
        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.IMAGE_JPEG)
                .body(new InputStreamResource(new FileInputStream(file)));
    }

    @RequestMapping(value = "/upload/posel/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getPoselImage(@PathVariable int id) throws FileNotFoundException {
        String path = "upload/posel/" + id + ".jpg";
        File file = new File(path);
        if (!file.exists()) throw new ImageNotFoundException();
        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.IMAGE_JPEG)
                .body(new InputStreamResource(new FileInputStream(file)));
    }
}