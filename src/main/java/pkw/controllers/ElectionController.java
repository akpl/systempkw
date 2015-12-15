package pkw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pkw.models.Election;
import pkw.models.ElectionDAO;

import javax.validation.Valid;

@Controller
public class ElectionController {
    @Autowired
    private ElectionDAO electionDAO;

    @RequestMapping(value = "/election")
     public String index() {
        return "election";
    }

    @RequestMapping(value = "/election/add", method = RequestMethod.POST)
    public String add(@Valid Election election, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "election";
        } else {
            election.setCreatorId(1);
            electionDAO.insert(election);
            return "election";
        }
    }
}
