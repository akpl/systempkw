package pkw.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Elimas on 2016-04-18.
 */
@Controller
@RequestMapping("/panel")
public class PanelController {

    @RequestMapping
    public String index(Model model) {
        model.addAttribute("view", "home");
        return "main";
    }
}
