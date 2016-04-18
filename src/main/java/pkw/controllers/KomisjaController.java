package pkw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pkw.models.Komisja;
import pkw.models.Okreg;
import pkw.models.Uzytkownik;
import pkw.repositories.KomisjaRepository;
import pkw.repositories.OkregRepository;
import pkw.repositories.PoziomDostepuRepository;
import pkw.repositories.UzytkownikRepository;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/panel")
public class KomisjaController {
    @Autowired
    private OkregRepository okregRepository;

    @Autowired
    private KomisjaRepository komisjaRepository;

    @Autowired
    private UzytkownikRepository uzytkownikRepository;

    @Autowired
    private PoziomDostepuRepository poziomDostepuRepository;

    @ModelAttribute("okregi")
    public Iterable<Okreg> okregi() {
        return okregRepository.findAll();
    }

    @RequestMapping(value = "/komisje")
    public String browse(Model model) {
        model.addAttribute("view", "komisje/przegladaj");
        return "main";
    }

    @RequestMapping(value = "/komisje/dodaj", method = RequestMethod.GET)
    public String dodajKomisje(@RequestParam(value = "okreg") int idOkregu, Komisja komisja, Model model) {
        List<Uzytkownik> przewodniczacy = uzytkownikRepository.dostepniPrzewodniczacyKomisji();
        model.addAttribute("usersAvailable", false);
        if (przewodniczacy.size() > 0) model.addAttribute("usersAvailable", true);
        model.addAttribute("czlonkowiePKW", przewodniczacy);
        model.addAttribute("idOkregu", idOkregu);
        model.addAttribute("view", "komisje/dodaj-komisja");
        return "main";
    }

    @RequestMapping(value = "/komisje/dodaj", method = RequestMethod.POST)
    public String dodajKomisje(@RequestParam(value = "okreg") int idOkregu, @Valid Komisja komisja, BindingResult bindingResult, Model model) {
        model.addAttribute("view", "komisje/dodaj-komisja");
        if (bindingResult.hasErrors()) {
            return "main";
        } else {
            Okreg okreg = okregRepository.findOne(idOkregu);
            komisja.setOkregWyborczy(okreg);
            Uzytkownik przewodniczacy = uzytkownikRepository.findOne(komisja.getPrzewodniczacyId());
            komisja.setPrzewodniczacy(przewodniczacy);
            komisjaRepository.save(komisja);
            model.addAttribute("linkPowrotny", "/komisje");
            model.addAttribute("view", "komisje/zapisano");
            return "main";
        }
    }

    @RequestMapping(value = "/komisje/usun")
    public String usunKomisje(@RequestParam(value = "id") int id, Model model) {
        model.addAttribute("view", "komisje/usunieto");
        model.addAttribute("success", false);
        if (komisjaRepository.exists(id)) {
            komisjaRepository.delete(id);
            model.addAttribute("success", true);
        } else {
            model.addAttribute("powod", "Wybrana komisja nie istnieje.");
        }
        return "main";
    }

    @RequestMapping(value = "/komisje/edytuj")
    public String edytujKomisje(@RequestParam(value = "id") int id, Model model) {
        model.addAttribute("view", "komisje/edytuj-komisja");
        model.addAttribute("edit", true);
        model.addAttribute("exists", false);
        if (komisjaRepository.exists(id)) {
            model.addAttribute("exists", true);
            Komisja komisjaDoEdycji = komisjaRepository.findOne(id);
            komisjaDoEdycji.setPrzewodniczacyId(komisjaDoEdycji.getPrzewodniczacy().getId());
            List<Uzytkownik> dostepniPrzewodniczacy = uzytkownikRepository.dostepniPrzewodniczacyKomisji();
            dostepniPrzewodniczacy.add(komisjaDoEdycji.getPrzewodniczacy());
            model.addAttribute("czlonkowiePKW", dostepniPrzewodniczacy);
            model.addAttribute("komisja", komisjaDoEdycji);
        }
        return "main";
    }

    @RequestMapping(value = "/komisje/edytuj", method = RequestMethod.POST)
    public String edytujKomisje(@RequestParam(value = "id") int id, @Valid Komisja komisja, BindingResult bindingResult, Model model) {
        model.addAttribute("view", "komisje/edytuj-komisja");
        model.addAttribute("edit", true);
        model.addAttribute("exists", false);
        if (komisjaRepository.exists(id)) {
            model.addAttribute("exists", true);
            Komisja komisjaDoEdycji = komisjaRepository.findOne(id);
            if (!bindingResult.hasErrors()) {
                komisja.setNr(id);
                komisja.setPrzewodniczacy(uzytkownikRepository.findOne(komisja.getPrzewodniczacyId()));
                komisja.setOkregWyborczy(komisjaDoEdycji.getOkregWyborczy());
                komisjaRepository.save(komisja);
                model.addAttribute("view", "komisje/zapisano");
            }
        }
        return "main";
    }

    @RequestMapping(value = "/komisje/okregi/dodaj", method = RequestMethod.GET)
    public String dodajOkreg(Okreg okreg, Model model) {
        model.addAttribute("view", "komisje/dodaj-okreg");
        return "main";
    }

    @RequestMapping(value = "/komisje/okregi/dodaj", method = RequestMethod.POST)
    public String dodajOkreg(@Valid Okreg okreg, BindingResult bindingResult, Model model) {
        model.addAttribute("view", "komisje/dodaj-okreg");
        if (bindingResult.hasErrors()) {
            return "main";
        } else {
            okregRepository.save(okreg);
            model.addAttribute("view", "komisje/zapisano");
            return "main";
        }
    }

    @RequestMapping(value = "/komisje/okregi/usun")
    public String usunOkreg(@RequestParam(value = "id") int id, Model model) {
        model.addAttribute("view", "komisje/usunieto");
        model.addAttribute("success", false);
        if (okregRepository.exists(id)) {
            if(okregRepository.findOne(id).getKomisje().isEmpty()) {
                okregRepository.delete(id);
                model.addAttribute("success", true);
            } else {
                model.addAttribute("powod", "Wybrany okręg zawiera komisje. Ze względów bezpieczeństwa należy usunąć je ręcznie przed usunięciem okręgu.");
            }
        } else {
            model.addAttribute("powod", "Wybrany okręg nie istnieje.");
        }

        return "main";
    }

    @RequestMapping(value = "/komisje/okregi/edytuj")
    public String edytujOkreg(@RequestParam(value = "id") int id, Model model) {
        model.addAttribute("view", "komisje/edytuj-okreg");
        model.addAttribute("edit", true);
        model.addAttribute("exists", false);
        if (okregRepository.exists(id)) {
            model.addAttribute("exists", true);
            Okreg okregDoEdycji = okregRepository.findOne(id);
            model.addAttribute("okreg", okregDoEdycji);
        }
        return "main";
    }

    @RequestMapping(value = "/komisje/okregi/edytuj", method = RequestMethod.POST)
    public String edytujOkreg(@RequestParam(value = "id") int id, @Valid Okreg okreg, BindingResult bindingResult, Model model) {
        model.addAttribute("view", "komisje/edytuj-okreg");
        model.addAttribute("edit", true);
        model.addAttribute("exists", false);
        if (okregRepository.exists(id)) {
            model.addAttribute("exists", true);
            if (!bindingResult.hasErrors()) {
                okreg.setNr(id);
                okregRepository.save(okreg);
                model.addAttribute("view", "komisje/zapisano");
            }
        }
        return "main";
    }
}
