package pkw.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping("/")
    public String index() {
        return "layout";
    }


    @RequestMapping("/test")
    public String test() {
        return "test test test";
    }
}