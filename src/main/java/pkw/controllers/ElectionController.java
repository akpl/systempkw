package pkw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pkw.models.Election;
import pkw.models.ElectionDAO;
import pkw.models.ElectionRepository;

import javax.validation.Valid;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class ElectionController {
    @Autowired
    private ElectionDAO electionDAO;

    @Autowired
    private ElectionRepository electionRepository;

    @ModelAttribute("elections")
    public Iterable<Election> elections() {
        return electionRepository.findAll();
    }

    @RequestMapping(value = "/election/browse")
    public String electionBrowse(Model model) {
        model.addAttribute("view", "election/browse");
        return "main";
    }

    @RequestMapping(value = "/election/add")
    public String electionAdd(Model model) {
        model.addAttribute("view", "election/add");
        return "main";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }

    @RequestMapping(value = "/election/add", method = RequestMethod.POST)
    public String add(@Valid Election election, BindingResult bindingResult, Model model) {
        model.addAttribute("view", "election/add");
        if (bindingResult.hasErrors()) {
            return "main";
        } else {
            model.addAttribute("success", true);
            election.setCreatorId(1);
            election.setCreationDate(new Date());
            electionRepository.save(election);
            return "main";
        }
    }
}