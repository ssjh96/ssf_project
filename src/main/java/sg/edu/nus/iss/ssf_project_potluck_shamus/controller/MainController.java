package sg.edu.nus.iss.ssf_project_potluck_shamus.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.GetMapping;



@Controller
@RequestMapping("")
public class MainController 
{
    @GetMapping("")
    public String displayMainPage() {
        return "main";
    }
}
