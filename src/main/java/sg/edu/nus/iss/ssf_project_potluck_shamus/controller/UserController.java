package sg.edu.nus.iss.ssf_project_potluck_shamus.controller;

import java.text.ParseException;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import sg.edu.nus.iss.ssf_project_potluck_shamus.model.UserModel;
import sg.edu.nus.iss.ssf_project_potluck_shamus.service.UserService;

import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController 
{

    @Autowired
    private UserService userService;

    Random rand = new Random();

    // LOGIN
    @GetMapping("/login")
    public String displayLogin(@RequestParam(value = "error", required = false) String error, 
                                @RequestParam (value = "registered", required = false) String registered, 
                                Model model) 
    {
        model.addAttribute("user", new UserModel());

        if (error != null)
        {
            model.addAttribute("errorMsg", "Invalid username of password. Please try again.");
        }

        if (registered != null)
        {
            model.addAttribute("registeredMsg", "Registration successful. Please login.");
        }

        return "login";
    }
    

    // REGISTRATION
    @GetMapping("/registration")
    public String displayRegistration(Model model) 
    {
        model.addAttribute("user", new UserModel()); // Bind new User object to registration form
        return "registration";
    }

    @PostMapping("/registration")
    public String handleRegistration(@Valid @ModelAttribute("user") UserModel user, 
                                    BindingResult bindingResult, 
                                    Model model) throws ParseException 
    {

        // if username already registered
        if (userService.usernameRegistered(user))
        {
            ObjectError error = new ObjectError("globalError", "Username is already taken, You may consider: %s".formatted(userService.suggestUsername(user)));
            bindingResult.addError(error);
        }

        // if email already registered
        if (userService.emailRegistered(user))
        {
            ObjectError error = new ObjectError("globalError", "Email is already taken, please use another.");
            bindingResult.addError(error);
        }

        if (bindingResult.hasErrors())
        {
            return "registration"; // Return registration form with errors
        }
        
        userService.register(user);

        return "redirect:/users/login?registered";
    }    
    
}
