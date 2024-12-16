package sg.edu.nus.iss.ssf_project_potluck_shamus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import sg.edu.nus.iss.ssf_project_potluck_shamus.model.UserModel;
import sg.edu.nus.iss.ssf_project_potluck_shamus.service.UserService;

import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;


@Controller
public class UserController 
{

    @Autowired
    private UserService userService;

    // LOGGING IN
    @GetMapping("/login")
    public String displayLogin(Model model) 
    {
        model.addAttribute("user", new UserModel());

        return "login_page";
    }

    @PostMapping("/login")
    public String handleLogin(@Valid @ModelAttribute("user") UserModel user, BindingResult bindingResult) 
    {
         if (bindingResult.hasErrors())
        {
            return "login_page"; // Return login form with errors
        }

        User
        
        System.out.println(user);
        return "main_page";
    }

    
    // REGISTRATION
    @GetMapping("/registration")
    public String displayRegistration(Model model) 
    {
        model.addAttribute("user", new UserModel());
        return "registraton_page";
    }

    @PostMapping("/login")
    public String handleRegistration(@Valid @ModelAttribute("user") UserModel user, BindingResult bindingResult) 
    {
         if (bindingResult.hasErrors())
        {
            return "registration_page"; // Return registration form with errors
        }

        UserModel existingUser = userService.findUsername(user.getUsername());

        if
        
        System.out.println(user);
        return "mainpage";
    }
    
}
