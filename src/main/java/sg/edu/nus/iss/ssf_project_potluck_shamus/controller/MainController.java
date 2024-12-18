package sg.edu.nus.iss.ssf_project_potluck_shamus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("")
public class MainController {

    @GetMapping("")
    public String displayMainPage() {
        return "main";
    }

    @GetMapping("/home")
    public String success(Model model) {
        model.addAttribute("username", "placeholder");
        return "home";
    }
    
    
    

}
