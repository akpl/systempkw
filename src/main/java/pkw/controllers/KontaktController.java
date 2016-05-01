package pkw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pkw.SendMail;
import pkw.models.Kontakt;
import pkw.models.Uzytkownik;
import pkw.repositories.PoziomDostepuRepository;
import pkw.repositories.UzytkownikRepository;

import javax.validation.Valid;

/**
 * Created by Elimas on 2016-04-17.
 */
@Controller
public class KontaktController {

    @Autowired
    private PoziomDostepuRepository poziomDostepuRepository;

    @Autowired
    private UzytkownikRepository uzytkownikRepository;

    @RequestMapping(value = "/kontakt", method = RequestMethod.GET)
    public String kontakt(Kontakt kontakt, Model model) {
        model.addAttribute("view", "kontakt/index");
        return "public/kontakt";
    }

    @RequestMapping(value = "/kontakt", method = RequestMethod.POST)
    public String kontakt(@Valid Kontakt kontakt, BindingResult bindingResult, Model model) {
        model.addAttribute("view", "kontakt/index");
        if (!bindingResult.hasErrors()) {
            //email będzie wysłany do pierwszego admina
            Uzytkownik admin = uzytkownikRepository.findFirstByPoziomDostepuOrderByIdAsc(poziomDostepuRepository.findOne(1));
            if (admin == null || admin.getEmail() == null || admin.getEmail().length() == 0) {
                model.addAttribute("error", true);
            } else {
                SendMail mail = new SendMail();
                mail.setContent(kontakt.getWiadomosc().replaceAll("(\r\n|\n\r|\r|\n)", "<br />"));
                mail.addRecipientToMail(admin.getEmail());
                mail.setReplyTo(kontakt.getNazwa() + " <" + kontakt.getEmail() + ">");
                mail.setSubject("[PKW][Kontakt] " + kontakt.getTemat());
                mail.sendEmail();
                model.addAttribute("success", true);
            }
        }
        return "public/kontakt";
    }
}
