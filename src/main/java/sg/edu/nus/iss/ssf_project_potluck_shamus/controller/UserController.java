package sg.edu.nus.iss.ssf_project_potluck_shamus.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import sg.edu.nus.iss.ssf_project_potluck_shamus.model.User;
import sg.edu.nus.iss.ssf_project_potluck_shamus.service.UserService;

import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/users")
public class UserController 
{

    @Autowired
    private UserService userService;

    // LOGIN
    @GetMapping("/login")
    public String displayLogin(Model model) 
    {
        model.addAttribute("user", new User());

        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, HttpSession httpSession, Model model) 
    {
         if (bindingResult.hasErrors())
        {
            return "login"; // Return login form with errors
        }

        String username = user.getUsername();
        String inputPassword = user.getPassword();

        User currentUser = userService.findUsername(username);
        System.out.println("User is >>>" + currentUser);

        // Check if username does not exist or password is wrong
        if (currentUser == null || !userService.authenticate(inputPassword, currentUser)) 
        {
            model.addAttribute("error", "Invalid username or password. Please try again.");

            return "login"; // Return login form with errors
        }
        
        httpSession.setAttribute("currentUser", currentUser);
        model.addAttribute("username", username);

        return "redirect:/success";
    }

    

    // REGISTRATION
    @GetMapping("/registration")
    public String displayRegistration(Model model) 
    {
        model.addAttribute("user", new User());

        return "registration";
    }

    @PostMapping("/registration")
    public String handleRegistration(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) 
    {
         if (bindingResult.hasErrors())
        {
            return "registration"; // Return registration form with errors
        }

        String username = user.getUsername();
        User newUser = userService.findUsername(username);

        // Check if username exist
        if (newUser != null)
        {
            model.addAttribute("error", "Username already exist. You may try " + username + UUID.randomUUID().toString().replace("-", "").substring(0, 4));

            return "registration";
        }

        userService.register(newUser);
        System.out.println("user registered >>>" + newUser);

        return "redirect:/users/login";
    }

    
}
