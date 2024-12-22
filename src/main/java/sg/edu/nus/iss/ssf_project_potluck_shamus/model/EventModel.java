package sg.edu.nus.iss.ssf_project_potluck_shamus.model;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import sg.edu.nus.iss.ssf_project_potluck_shamus.util.InviteStatus;

public class EventModel {

    // Non-Validated Attributes
    private String id;
    private String host;
    private List<String> participants;
    private Map<String, InviteStatus> inviteStatus; // User : PENDING/ACCEPTED/REJECTED
    
    // Validated Attributes
    @NotBlank(message = "Title must not be blank.")
    @Size(max = 30, message = "Title must be less than 30 characters long.")
    private String title;

    @Future(message = "Post date must be a future date")
    @NotNull(message = "You must set your posting date.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    @NotBlank(message = "Location must not be empty.")
    @Size(max = 50, message = "Location must be less than 50 characters long.")
    private String location;

    

    // CONSTRUCTORS
    // Default Constructor
    public EventModel() 
    {
        this.id = UUID.randomUUID().toString().replace("-", "").substring(0,  4);
    }

    // Constructor for new event
    public EventModel(String host, List<String> participants, Map<String, InviteStatus> inviteStatus, String title,
            Date date, String location) 
    {
        this.id = UUID.randomUUID().toString().replace("-", "").substring(0,  4);
        this.host = host;
        this.participants = participants;
        this.inviteStatus = inviteStatus;

        this.title = title;
        this.date = date;
        this.location = location;
    }

    // Constructor for updating or rebuilding event object from data stored in Redis DB
    public EventModel(String id, String host, List<String> participants, Map<String, InviteStatus> inviteStatus, String title,
            Date date, String location) 
    {
        this.id = id;
        this.host = host;
        this.participants = participants;
        this.inviteStatus = inviteStatus;

        this.title = title;
        this.date = date;
        this.location = location;
    }


    
    // GETTERS SETTERS
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public Map<String, InviteStatus> getInviteStatus() {
        return inviteStatus;
    }

    public void setInviteStatus(Map<String, InviteStatus> inviteStatus) {
        this.inviteStatus = inviteStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    
}
