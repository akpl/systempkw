package pkw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pkw.models.Election;
import pkw.models.ElectionDAO;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class ElectionController {
    @Autowired
    private ElectionDAO electionDAO;

    @RequestMapping(value = "/election")
     public String index() {
        return "election";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }

    @RequestMapping(value = "/election/add", method = RequestMethod.POST)
    public String add(@Valid Election election, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
           // model.addAttribute()
            //bindingResult.
            return "election";
        } else {
            election.setCreatorId(1);
            electionDAO.insert(election);
            return "election";
        }
    }
}
