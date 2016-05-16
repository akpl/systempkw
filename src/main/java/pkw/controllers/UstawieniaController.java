package pkw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pkw.forms.UstawieniaKonta;
import pkw.models.Uzytkownik;
import pkw.repositories.UzytkownikRepository;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/panel")
public class UstawieniaController {

    @Autowired
    private UzytkownikRepository uzytkownikRepository;

    @RequestMapping(value = "/ustawienia", method = RequestMethod.GET)
    public String ustawienia(@AuthenticationPrincipal UserDetails loggedUser, Model model) {
        Uzytkownik uzytkownik = uzytkownikRepository.findOneByLogin(loggedUser.getUsername());
        UstawieniaKonta ustawieniaKonta = new UstawieniaKonta();
        ustawieniaKonta.setEmail(uzytkownik.getEmail());
        model.addAttribute("ustawieniaKonta", ustawieniaKonta);
        model.addAttribute("view", "ustawienia");
        return "main";
    }

    @RequestMapping(value = "/ustawienia", method = RequestMethod.POST)
    public String ustawienia(@Valid UstawieniaKonta ustawienieniaKonta, BindingResult bindingResult, @AuthenticationPrincipal UserDetails loggedUser, Model model) {
        model.addAttribute("view", "ustawienia");
        if (ustawienieniaKonta.getHaslo() != null && ustawienieniaKonta.getHaslo().length() > 0 && ustawienieniaKonta.getHaslo().length() < 6) bindingResult.rejectValue("haslo", "password.length.short", "Minimalna długość hasła to 6 znaków.");
        if (ustawienieniaKonta.getHaslo() != null && ustawienieniaKonta.getPowtorzHaslo() != null && (ustawienieniaKonta.getHaslo().length() > 0 || ustawienieniaKonta.getPowtorzHaslo().length() > 0)) {
            if (!ustawienieniaKonta.getHaslo().equals(ustawienieniaKonta.getPowtorzHaslo())) {
                bindingResult.rejectValue("haslo", "password.diffrent", "Hasła są różne.");
                bindingResult.rejectValue("powtorzHaslo", "password.diffrent", "Hasła są różne.");
            }
        }
        if (!bindingResult.hasErrors()) {
            ShaPasswordEncoder passwordEncoder = new ShaPasswordEncoder(256);
            Uzytkownik uzytkownik = uzytkownikRepository.findOneByLogin(loggedUser.getUsername());
            uzytkownik.setEmail(ustawienieniaKonta.getEmail());
            if (ustawienieniaKonta.getHaslo() != null && ustawienieniaKonta.getHaslo().length() > 0) uzytkownik.setHaslo(passwordEncoder.encodePassword(ustawienieniaKonta.getHaslo(), null));
            uzytkownikRepository.save(uzytkownik);
            model.addAttribute("success", true);
        }
        return "main";
    }
}
