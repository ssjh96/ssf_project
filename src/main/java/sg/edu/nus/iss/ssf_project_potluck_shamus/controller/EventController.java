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
import sg.edu.nus.iss.ssf_project_potluck_shamus.model.MealModel;
import sg.edu.nus.iss.ssf_project_potluck_shamus.model.UserModel;
import sg.edu.nus.iss.ssf_project_potluck_shamus.service.CategoryService;
import sg.edu.nus.iss.ssf_project_potluck_shamus.service.EventService;
import sg.edu.nus.iss.ssf_project_potluck_shamus.service.MealService;
import sg.edu.nus.iss.ssf_project_potluck_shamus.service.UserService;
import sg.edu.nus.iss.ssf_project_potluck_shamus.util.InviteStatus;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/events")
public class EventController 
{
    @Autowired
    private EventService eventService;

    @Autowired 
    private UserService userService;

    @Autowired
    private MealService mealService;

    @Autowired
    private CategoryService categoryService;



    // HOME PAGE
    // 1. Show home page: (a) Events, (b) Notifications, (c) Messages, 
    // (d) Methods on same page: (i) Delete, (ii) Accept, (iii) Reject
    @GetMapping("/home")
    public String showHomePage(@AuthenticationPrincipal UserDetails userDetails, 
                                @RequestParam (value = "deleted", required = false) String deleted,
                                @RequestParam (value = "notDeleted", required = false) String notDeleted,
                                @RequestParam (value = "created", required = false) String created,
                                @RequestParam (value = "accepted", required = false) String accepted,
                                @RequestParam (value = "rejected", required = false) String rejected,
                                Model model) throws ParseException 
    {
        String username = userDetails.getUsername();
        model.addAttribute("username", username);
        
        UserModel user = userService.findUser(username);

        // If user is ADMIN, get all events
        if (user.getRole().equals("ADMIN"))
        {
            List<EventModel> events = eventService.getAllEvents();
            model.addAttribute("events", events);
        }
        else // Else user is USER, get only participating events
        {
            List<EventModel> events = eventService.getParticipatingEvents(username);
            model.addAttribute("events", events);
        }
        

        // Get all pending invitations sent to user
        List<EventModel> pendingEvents = eventService.getPendingInvites(username);
        model.addAttribute("pendingEvents", pendingEvents);


        // Endpoint checks to display messages
        if (deleted != null)
        {
            model.addAttribute("message", "Event successfully deleted.");
        }

        if (notDeleted != null)
        {
            model.addAttribute("notDeletedMsg", "Error, only the host or admin can delete the event.");
        }

        if (created != null)
        {
            model.addAttribute("message", "Event succesfully created.");
        }

        if (accepted != null)
        {
            model.addAttribute("message", "Event accepted succesfully.");
        }

        if (rejected != null)
        {
            model.addAttribute("message", "Event rejected succesfully.");
        }


        return"home";
    }
    
    
    // CREATE PAGE
    @GetMapping("/create")
    public String showCreateForm(@AuthenticationPrincipal UserDetails userDetails,
                                Model model) 
    {
        String username = userDetails.getUsername();
        model.addAttribute("username", username);
        model.addAttribute("event", new EventModel());
        return "createform";
    }

    @PostMapping("/create")
    public String postCreateForm(@Valid @ModelAttribute ("event") EventModel event,
                                BindingResult bindingResult, 
                                @AuthenticationPrincipal UserDetails userDetails, 
                                @RequestParam (value = "inputParticipants", required = false) String inputParticipants, 
                                Model model) throws ParseException 
    {
        List<String> participantsList = new ArrayList<>();
        List<String> invalidUsers = new ArrayList<>();

        Map<String, String> contributions = new HashMap<>();
        Map<String, InviteStatus> inviteStatus = new HashMap<>();
        

        // Validation checks
        if (bindingResult.hasErrors())
        {
            return "createform"; 
        }

        // Set user who created event as host
        String host = userDetails.getUsername();
        event.setHost(host);
        participantsList.add(host);

        contributions.put(host, "No contribution yet");
        inviteStatus.put(host, InviteStatus.ACCEPTED);


        // If participants are added and not just blank
        if (inputParticipants != null && !inputParticipants.isBlank())
        {
            String[] participantsArray = inputParticipants.split(",");

            for (String participant : participantsArray)
            {
                participant = participant.trim();

                // Check that user is not trying to add themself to participants list
                if(participant.equals(host))
                {
                    ObjectError error = new ObjectError("globalError", "Cannot add yourself: %s".formatted(host));
                    bindingResult.addError(error);

                    return "createform";
                }

                // Check that the participant user is trying to add exist in Redis DB
                if (userService.findUser(participant)!= null)
                {
                    participantsList.add(participant);
                    contributions.put(participant, "No contribution yet");
                    inviteStatus.put(participant, InviteStatus.PENDING);
                }

                else // Else add participant as invalid user
                {
                    invalidUsers.add(participant);
                }
            }
        }

        // Custom Validation: Host entered invalid usernames, show who is invalid.
        if(!invalidUsers.isEmpty())
        {
            ObjectError error = new ObjectError("globalError", "Users not found: %s".formatted(invalidUsers));
            bindingResult.addError(error);

            return "createform";
        }

        event.setParticipants(participantsList);
        event.setContributions(contributions);
        event.setInviteStatus(inviteStatus);

        eventService.createEvent(event);
        
        return "redirect:/events/home?created";
    }


    @PostMapping("/delete")
    public String deleteEvent(@AuthenticationPrincipal UserDetails userDetails, 
                                @RequestParam("eventId") String eventId) throws ParseException 
    {
        String username = userDetails.getUsername();
        
        if (!eventService.deleteEvent(eventId, username))
        {
            return "redirect:/events/home?notDeleted";
        }

        return "redirect:/events/home?deleted";
    }
    

    @PostMapping("/accept")
    public String acceptInvite(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam("eventId") String eventId) throws ParseException 
    {
        String username = userDetails.getUsername();
        eventService.acceptInvite(eventId, username);

        return "redirect:/events/home?accepted";
    }
    

    @PostMapping("/reject")
    public String rejectInvite(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam("eventId") String eventId) throws ParseException 
    {
        String username = userDetails.getUsername();
        eventService.rejectInvite(eventId, username);

        return "redirect:/events/home?rejected";
    }


    // API PAGE
    @GetMapping("/viewapi")
    public String viewApiData(Model model) throws ParseException 
    {       
        model.addAttribute("categories", categoryService.fetchCategories());

        return "viewapi";
    }
    

    // EVENT PAGE
    @GetMapping("/view/{eventId}")
    public String viewEvent(@AuthenticationPrincipal UserDetails userDetails,
                            @PathVariable ("eventId") String eventId,
                            Model model) throws ParseException 
    {
        String username = userDetails.getUsername();
        model.addAttribute("username", username);

        EventModel event = eventService.getEvent(eventId);
        model.addAttribute("event", event);

        Map<String, String> pContributions = eventService.getParticipatingContributions(eventId);
        model.addAttribute("pContributions", pContributions);

        return "viewevent";
    }

    
    // SELECT CATEGORY PAGE
    @GetMapping("/selectcategory/{eventId}")
    public String showContributionsForm(@AuthenticationPrincipal UserDetails userDetails,
                                        @PathVariable("eventId") String eventId, 
                                        Model model) throws ParseException 
    {
        String username = userDetails.getUsername();
        EventModel event = eventService.getEvent(eventId);


        // Check user accepted invite before viewing suggestions
        InviteStatus inviteStatus = event.getInviteStatus().get(username);
        
        if (inviteStatus != InviteStatus.ACCEPTED)
        {
            model.addAttribute("errorMsg", "Please accept the invitation from home page first.");

            model.addAttribute("username", username);
            model.addAttribute("event", event);

            Map<String, String> pContributions = eventService.getParticipatingContributions(eventId);
            model.addAttribute("pContributions", pContributions);

            return "viewevent";
        }


        model.addAttribute("username", username);
        model.addAttribute("event", event);
        model.addAttribute("categories", categoryService.fetchCategories());

        return "selectcategory";
    }


    // BROWSE CATEGORY PAGE
    @GetMapping("/browsecategory/{eventId}") 
    public String browseCategory(@AuthenticationPrincipal UserDetails userDetails,
                                @PathVariable ("eventId") String eventId,
                                @RequestParam ("category") String category,
                                Model model) throws ParseException
    {
        String username = userDetails.getUsername();
        EventModel event = eventService.getEvent(eventId);
        List<MealModel> meals = mealService.filterByCategory(category);

        model.addAttribute("username", username);
        model.addAttribute("event", event);
        model.addAttribute("category", category);
        model.addAttribute("meals", meals);

        return "browsecategory";
    }
    
    @GetMapping("/mealdetails/{eventId}")
    public String showMealDetails(@AuthenticationPrincipal UserDetails userDetails,
                                @PathVariable ("eventId") String eventId,
                                @RequestParam ("category") String category,
                                @RequestParam ("mealId") String mealId,
                                Model model) throws ParseException 
    {
        String username = userDetails.getUsername();
        EventModel event = eventService.getEvent(eventId);
        MealModel meal = mealService.getById(mealId);

        System.out.println("Meals consist: " + meal);

        if (meal == null)

        model.addAttribute("username", username);
        model.addAttribute("event", event);
        model.addAttribute("category", category);
        model.addAttribute("meal", meal);

        return "mealdetails";
    }

    @PostMapping("/addsuggestion") 
    public String addSuggestion(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam ("eventId") String eventId,
                                @RequestParam ("mealId") String mealId) throws ParseException 
    {
        String username = userDetails.getUsername();
        EventModel event = eventService.getEvent(eventId);
        Map<String, String> contributions = event.getContributions();

        MealModel meal = mealService.getById(mealId);
        contributions.put(username, meal.getName());
        eventService.createEvent(event);

        return "redirect:/events/view/" + eventId;
    }      


    @GetMapping("/viewrecipe/{eventId}")
    public String viewRecipe(@AuthenticationPrincipal UserDetails userDetails,
                            @PathVariable("eventId") String eventId,
                            @RequestParam("mealName") String mealName,
                            Model model) throws ParseException 
    {
        String username = userDetails.getUsername();
        EventModel event = eventService.getEvent(eventId);

        if (mealName.equals("No contribution yet"))
        {
            model.addAttribute("errorMsg", "A contribution has not been added.");

            model.addAttribute("username", username);
            model.addAttribute("event", event);

            Map<String, String> pContributions = eventService.getParticipatingContributions(eventId);
            model.addAttribute("pContributions", pContributions);

            return "viewevent";
        }

        List<MealModel> meals = mealService.getByName(mealName);
        MealModel meal = meals.get(0);

        String mealId = meal.getId();
        String category = meal.getCategory();
        

        return "redirect:/events/mealdetails/" + eventId + "?category=" + category + "&mealId=" + mealId;
    }

}
