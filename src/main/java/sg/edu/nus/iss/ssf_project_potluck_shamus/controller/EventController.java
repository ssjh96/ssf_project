package sg.edu.nus.iss.ssf_project_potluck_shamus.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;

import sg.edu.nus.iss.ssf_project_potluck_shamus.model.EventModel;
import sg.edu.nus.iss.ssf_project_potluck_shamus.service.EventService;
import sg.edu.nus.iss.ssf_project_potluck_shamus.service.UserService;
import sg.edu.nus.iss.ssf_project_potluck_shamus.util.InviteStatus;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
@RequestMapping("/events")
public class EventController 
{
    @Autowired
    private EventService eventService;

    @Autowired 
    private UserService userService;

    // HOME PAGE
    @GetMapping("/home")
    public String showHomePage(@AuthenticationPrincipal UserDetails userDetails, Model model) throws ParseException 
    {
        String username = userDetails.getUsername();
        model.addAttribute("username", username);

        // Get all events user is participating in
        List<EventModel> events = eventService.getParticipatingEvents(username);
        model.addAttribute("events", events);

        // Get all pending invitations sent to user
        List<EventModel> notifications = eventService.getPendingInvites(username);
        model.addAttribute("notifications", notifications);

        return"home";
    }
    
    // CREATE PAGE
    @GetMapping("/create")
    public String showCreateForm(Model model) 
    {
        model.addAttribute("event", new EventModel());
        return "createform";
    }

    @PostMapping("/create")
    public String postCreateForm(@Valid @ModelAttribute ("events") EventModel event, 
                                @AuthenticationPrincipal UserDetails userDetails, 
                                @RequestParam ("participants") String participantsInput, 
                                BindingResult bindingResult,
                                Model model) throws ParseException 
    {
        List<String> participants = new ArrayList<>();
        List<String> invalidUsers = new ArrayList<>();
        Map<String, InviteStatus> inviteStatus = new HashMap<>();

        // Set user who created event as host
        String host = userDetails.getUsername();
        event.setHost(host);
        participants.add(host);
        inviteStatus.put(host, InviteStatus.ACCEPTED);


        // Check added participants
        if (participantsInput != null)
        {
            String[] participantsArray = participantsInput.split(",");

            for (String p : participantsArray)
            {
                p = p.trim();

                if (userService.findUser(p)!= null)
                {
                    eventService.sendInvite(event.getId(), p);
                }

                else
                {
                    invalidUsers.add(p);
                }
            }
        }

        if(!invalidUsers.isEmpty())
        {
            ObjectError error = new ObjectError("globalError", "Users not found: %s".formatted(invalidUsers));
            bindingResult.addError(error);
        }

        if (bindingResult.hasErrors())
        {
            return "createform"; // Return creation form with errors
        }

        eventService.createEvent(event);
        
        return "redirect:/events/home";
    }
    
    
    

}
