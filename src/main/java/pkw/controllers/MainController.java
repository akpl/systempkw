package pkw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pkw.ImageNotFoundException;
import pkw.models.*;
import pkw.repositories.NewsletterRepository;
import pkw.repositories.WyboryRepository;

import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

@Controller
public class MainController {


    @Autowired
    private NewsletterRepository newsletterRepository;

    @Autowired
    private WyboryRepository wyboryRepository;

    @ModelAttribute("nextWybory")
    public Wybory nextWybory() {
        List<Wybory> wybory = wyboryRepository.findNextPrezydenckieOrParlamentarne();
        if (wybory.size() > 0) return wybory.get(0);
        else return null;
    }

    @ModelAttribute("nextParlamentarne")
    public Wybory nextParlamentarne() {
        List<Wybory> wybory = wyboryRepository.findNextParlamentarne();
        for (Wybory w : wybory) {
            for (Komitet komitet : w.getKomitety()) {
                for (KandydatPosel kp : komitet.getKandydaciPosel()) {
                    for (WynikiPosel wynikiPosel : kp.getWyniki()) {
                        if (wynikiPosel.getLiczbaGlosow() > 0) return w;
                    }
                }
            }
        }
        return null;
    }

    @ModelAttribute("nextPrezydenckie")
    public Wybory nextPrezydenckie() {
        List<Wybory> wybory = wyboryRepository.findNextPrezydenckie();
        for (Wybory w : wybory) {
            for (KandydatPrezydent kp : w.getKandydaciPrezydent()) {
                if (kp.getWynikLaczny().getLiczbaGlosow() > 0) return w;
            }
        }
        return null;
    }

    @ModelAttribute("nextReferendum")
    public Wybory nextReferendum() {
        List<Wybory> wybory = wyboryRepository.findNextReferendum();
        for (Wybory w : wybory) {
            for (PytanieReferendalne p : w.getPytaniaReferendalne()) {
                if (p.getWynikLaczny().getOdpowiedziNie() + p.getWynikLaczny().getOdpowiedziTak() > 0) return w;
            }
        }
        return null;
    }

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