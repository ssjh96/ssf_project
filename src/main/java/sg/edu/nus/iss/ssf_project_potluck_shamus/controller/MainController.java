package sg.edu.nus.iss.ssf_project_potluck_shamus.controller;

import org.springframework.stereotype.Controller;
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

    @GetMapping("/sucess")
    public String success() {
        return "success";
    }
    
    
    

}
