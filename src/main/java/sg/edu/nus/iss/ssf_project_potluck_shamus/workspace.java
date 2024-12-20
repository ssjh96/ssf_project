// package sg.edu.nus.iss.ssf_project_potluck_shamus;

// public class workspace {
    
// }

// @PostMapping("/login")
    // public String handleLogin(@Valid @ModelAttribute("user") UserModel user, BindingResult bindingResult, HttpSession httpSession, Model model) throws ParseException 
    // {
    //      if (bindingResult.hasErrors())
    //     {
    //         return "login"; // Return login form with errors
    //     }

    //     // inputs
    //     String inputUsername = user.getUsername();
    //     String inputPassword = user.getPassword();

    //     // Check user exist
    //     UserModel existingUser  = userService.findUser(inputUsername);
    //     String existingUsername = existingUser.getUsername();

    //     if (existingUsername == null)
    //     {
    //         model.addAttribute("errorMsg", "Invalid username or password. Please try again.");

    //         return "login"; // Return login form with errors
    //     }

    //     if (existingUsername != null)
    //     {
    //         System.out.println("User is >>>" + existingUsername);

    //         if (!userService.authenticate(inputPassword, existingUser))
    //         {
    //             model.addAttribute("errorMsg", "Invalid username or password. Please try again.");

    //             return "login"; // Return login form with errors
    //         }

    //         httpSession.setAttribute("currentUser", existingUser);
    //         model.addAttribute("username", existingUsername);
    //     }

    //     return "redirect:/home";  
    // }

    // public Boolean authenticate(String inputPassword, UserModel user)
    // {     
    //     // check if user input passsword when encoded, matches the encoded pw stored in redis
    //     return passwordEncoder.matches(inputPassword, user.getPassword()); 
    // }
