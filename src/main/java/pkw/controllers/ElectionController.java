package pkw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pkw.models.Election;
import pkw.models.ElectionDAO;

@Controller
public class ElectionController {
    @Autowired
    private ElectionDAO electionDAO;

    @RequestMapping(value = "/election")
     public String index() {
        return "election";
    }

    @RequestMapping(value = "/election/add", method = RequestMethod.POST)
    public String add(ModelMap model, Election election) {
        election.setCreatorId(1);
        electionDAO.insert(election);
        return "election";
    }
}
