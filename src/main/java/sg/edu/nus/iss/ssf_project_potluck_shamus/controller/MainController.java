package sg.edu.nus.iss.ssf_project_potluck_shamus.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import sg.edu.nus.iss.ssf_project_potluck_shamus.model.UserModel;

import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("")
public class MainController {

    @GetMapping("")
    public String displayMainPage() {
        return "main";
    }

    @GetMapping("/home")
    public String success(Model model, @AuthenticationPrincipal UserDetails userDetails) 
    {
        String username = userDetails.getUsername();
        model.addAttribute("username", username);
        
        return "home";
    }
    
    

}
