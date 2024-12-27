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

import sg.edu.nus.iss.ssf_project_potluck_shamus.model.CategoryModel;
import sg.edu.nus.iss.ssf_project_potluck_shamus.model.EventModel;
import sg.edu.nus.iss.ssf_project_potluck_shamus.model.MealModel;
import sg.edu.nus.iss.ssf_project_potluck_shamus.service.CategoryService;
import sg.edu.nus.iss.ssf_project_potluck_shamus.service.EventService;
import sg.edu.nus.iss.ssf_project_potluck_shamus.service.MealService;
import sg.edu.nus.iss.ssf_project_potluck_shamus.service.UserService;
import sg.edu.nus.iss.ssf_project_potluck_shamus.util.InviteStatus;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @Autowired
    private MealService mealService;

    @Autowired
    private CategoryService categoryService;

    // HOME PAGE
    @GetMapping("/home")
    public String showHomePage(@AuthenticationPrincipal UserDetails userDetails, 
                                @RequestParam (value = "deleted", required = false) String deleted,
                                @RequestParam (value = "notDeleted", required = false) String notDeleted,
                                Model model) throws ParseException 
    {
        String username = userDetails.getUsername();
        model.addAttribute("username", username);

        // Get all events user is participating in
        List<EventModel> events = eventService.getParticipatingEvents(username);
        model.addAttribute("events", events);

        // Get all pending invitations sent to user
        List<EventModel> pendingEvents = eventService.getPendingInvites(username);
        model.addAttribute("pendingEvents", pendingEvents);

        if (deleted != null)
        {
            model.addAttribute("deletedMsg", "Event successfully deleted.");
        }

        if (notDeleted != null)
        {
            model.addAttribute("notDeletedMsg", "Error, only the host can delete the event.");
        }

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
        

        if (bindingResult.hasErrors())
        {
            return "createform"; // Return creation form with errors
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

                if(participant.equals(host))
                {
                    ObjectError error = new ObjectError("globalError", "Cannot add yourself: %s".formatted(host));
                    bindingResult.addError(error);

                    // invalidUsers.add(host);
                    return "createform";
                }

                if (userService.findUser(participant)!= null)
                {
                    participantsList.add(participant);
                    contributions.put(participant, "No contribution yet");
                    inviteStatus.put(participant, InviteStatus.PENDING);
                }

                else
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
        
        return "redirect:/events/home";
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

        return "redirect:/events/home";
    }
    
    @PostMapping("/reject")
    public String rejectInvite(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam("eventId") String eventId) throws ParseException 
    {
        String username = userDetails.getUsername();
        eventService.rejectInvite(eventId, username);

        return "redirect:/events/home";
    }

    @GetMapping("/view")
    public String viewEvent(@AuthenticationPrincipal UserDetails userDetails,
                            @RequestParam ("eventId") String eventId,
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

    // /events/add?eventId=12345 ?key=value
    @GetMapping("/selectcategory")
    public String showContributionsForm(@AuthenticationPrincipal UserDetails userDetails,
                                        @RequestParam("eventId") String eventId, 
                                        Model model) throws ParseException 
    {
        String username = userDetails.getUsername();
        EventModel event = eventService.getEvent(eventId);

        model.addAttribute("username", username);
        model.addAttribute("event", event);
        model.addAttribute("categories", categoryService.fetchCategories());

        return "selectcategory";
    }


    @PostMapping("/addcontribution")
    public String postContributionsForm(@AuthenticationPrincipal UserDetails userDetails,
                                        @RequestParam ("eventId") String eventId,
                                        @RequestParam ("mealId") String mealId) throws ParseException 
    {
        String username = userDetails.getUsername();
        EventModel event = eventService.getEvent(eventId);
        Map<String, String> contributions = event.getContributions();

        MealModel meal = mealService.getById(mealId);
        contributions.put(username, meal.getName());
        eventService.createEvent(event);

        return "redirect:/events/view?eventId=" + eventId;
    }
    

    @GetMapping("/browsecategory") 
    public String browseCategory(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam ("eventId") String eventId,
                                @RequestParam ("category") String category,
                                Model model) throws ParseException
    {
        String username = userDetails.getUsername();
        EventModel event = eventService.getEvent(eventId);
        List<MealModel> meals = mealService.filterByCategory(category);

        model.addAttribute("username", username);
        model.addAttribute("event", event);
        model.addAttribute("meals", meals);

        return "browsecategory";
    }
    
    @GetMapping("/mealdetail")
    public String getMethodName(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam ("eventId") String eventId,
                                @RequestParam ("mealId") String mealId,
                                Model model) throws ParseException 
    {
        String username = userDetails.getUsername();
        EventModel event = eventService.getEvent(eventId);
        MealModel meal = mealService.getById(mealId);

        System.out.println("Meals consist: " + meal);

        model.addAttribute("username", username);
        model.addAttribute("event", event);
        model.addAttribute("meal", meal);

        return "mealdetail";
    }
    


    // TEST API CALL
    @GetMapping("/test_c") // http://localhost:3000/events/test_c?c=seafood
    @ResponseBody
    public List<MealModel> testByCategory(@RequestParam ("c") String category) {
        return mealService.filterByCategory(category);
    }
    
    @GetMapping("/test_i") // http://localhost:3000/events/test_i?i=chicken
    @ResponseBody
    public List<MealModel> testByIngredient(@RequestParam ("i") String ingredient) {
        return mealService.filterByIngredient(ingredient);
    }

    @GetMapping("/test_a") // http://localhost:3000/events/test_a?a=canadian
    @ResponseBody
    public List<MealModel> testByArea(@RequestParam ("a") String area) {
        return mealService.filterByArea(area);
    }

    @GetMapping("/test_ac") // http://localhost:3000/events/test_ac
    @ResponseBody
    public List<CategoryModel> getAllCategories() 
    {
        return categoryService.fetchCategories();
    }
    
}
