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

    @GetMapping("/home")
    public String success(Model model, @AuthenticationPrincipal UserDetails userDetails) 
    {
        String username = userDetails.getUsername();
        model.addAttribute("username", username);
        
        return "home";
    }

    // e.g. On roles
    // @GetMapping("/delete/{title}")
    // @PreAuthorize("hasRole('ADMIN')")
    // public String delete(@PathVariable String title) {
    //     someService.delete('title');
    //     return "redirect:/somepage"
    // }
    // 
    // html: <td sec:authorize="hasRole('ADMIN')"><button><a th:href="xxx"
    // spring security extra?
    
    
    

}
