package sg.edu.nus.iss.ssf_project_potluck_shamus.service;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import sg.edu.nus.iss.ssf_project_potluck_shamus.constant.Constant;
import sg.edu.nus.iss.ssf_project_potluck_shamus.model.EventModel;
import sg.edu.nus.iss.ssf_project_potluck_shamus.repository.MapRepo;
import sg.edu.nus.iss.ssf_project_potluck_shamus.util.InviteStatus;

@Service
public class EventService 
{
    @Autowired
    private MapRepo mapRepo;

    String redisKey = Constant.eventsKey;



    private String serialiseEvent(EventModel event) 
    {
        // Convert List<String> participants into JsonArray ["x", "y", "z"]
        JsonArrayBuilder jab = Json.createArrayBuilder();

        for (String p : event.getParticipants())
        {
            jab.add(p);
        }

        JsonArray jParticipantsArray = jab.build();

        // Convert Map<String, String> invite status into JsonObject {"x" : "pending"}
        JsonObjectBuilder job = Json.createObjectBuilder();

        for (Entry<String, InviteStatus> entry : event.getInviteStatus().entrySet())
        {
            String participant = entry.getKey();
            String inviteStatus = entry.getValue().toString();

            job.add(participant, inviteStatus);
        }
        
        JsonObject jInviteStatusObject = job.build();

        // Serialised event object
        String eventJsonString = Json.createObjectBuilder()
                .add("id", event.getId())
                .add("host", event.getHost())
                .add("participants", jParticipantsArray) // List<String>
                .add("inviteStatus", jInviteStatusObject) // Map <String, String>
                .add("title", event.getTitle())
                .add("date", event.getDate().getTime()) // Date Format stored as Long
                .add("location", event.getLocation())
                .build()
                .toString();

        return eventJsonString;
    }



    private EventModel deserialiseEvent(String eventJsonString) throws ParseException
    {
        JsonReader reader = Json.createReader(new StringReader(eventJsonString));
        JsonObject jsonObject = reader.readObject();

        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // Extract String attributes
        String id = jsonObject.getString("id");
        String host = jsonObject.getString("host");
        String title = jsonObject.getString("title");
        String location = jsonObject.getString("location");

        // Extract Date Attribute
        Long dateLong = jsonObject.getJsonNumber("date").longValue();
        Date date = new Date(dateLong);

        // Extract List Attribute
        JsonArray jParticipantsArray = jsonObject.getJsonArray("participants");
        System.out.println("jParticipantArray is >>>" + jParticipantsArray);
        List<String> participants = new ArrayList<>();

        for (int i = 0; i < jParticipantsArray.size(); i++)
        {
            participants.add(jParticipantsArray.getString(i));
        }
        System.out.println("participants is >>>" + participants);

        // Extract Map<String, String> Attribute
        JsonObject jInviteStatusObject = jsonObject.getJsonObject("inviteStatus");
        System.out.println("jInviteStatusObject is >>>" + jInviteStatusObject);
        Map<String, InviteStatus> inviteStatus = new HashMap<>();

        for (Entry<String, JsonValue> entry : jInviteStatusObject.entrySet())
        {  
            JsonString status = (JsonString) entry.getValue();
            inviteStatus.put(entry.getKey(), InviteStatus.valueOf(status.getString()));
        }
        System.out.println("inviteStatus is >>>" + inviteStatus);

        // Return new Event object
        return new EventModel(id, host, participants, inviteStatus, title, date, location);
    }



    public void createEvent(EventModel event)
    {
        String fieldKey = redisKey + ":" + event.getId();
        mapRepo.put(redisKey, fieldKey, serialiseEvent(event));
    }


    
    public Boolean deleteEvent(String eventId)
    {
        String fieldKey = redisKey + ":" + eventId;
        return mapRepo.delete(redisKey, fieldKey);
    }



    // Get events that specified user is participating in
    public List<EventModel> getParticipatingEvents(String username) throws ParseException
    {
        // Retrieve from redis db all events {"events:xyz" : "stringified event json"}
        Map<Object, Object> allEvents = mapRepo.getAll(redisKey);

        List<EventModel> eventsParticipating = new ArrayList<>();

        for (Entry<Object, Object> entry : allEvents.entrySet())
        {
            String eventJsonString = entry.getValue().toString();
            EventModel event = deserialiseEvent(eventJsonString);

            // if given user is the host or included in the list of participants, add to list of participating event
            if (event.getHost().equals(username) || event.getParticipants().contains(username)) 
            {
                eventsParticipating.add(event);
            }
        }

        return eventsParticipating;
    }



    // Get pending invites that specified user has
    public List<EventModel> getPendingInvites (String username) throws ParseException
    {
        // Retrieve from redis db all events {"events:xyz" : "stringified event json"}
        Map<Object, Object> allEvents = mapRepo.getAll(redisKey);

        List<EventModel> pendingInvites = new ArrayList<>();

        for (Entry<Object, Object> entry : allEvents.entrySet())
        {
            String eventJsonString = entry.getValue().toString();
            EventModel event = deserialiseEvent(eventJsonString);

            // if given user is a member of the event, and his/her status is still pending, add to list of pending invites
            if (event.getInviteStatus().containsKey(username) && event.getInviteStatus().get(username).equals(InviteStatus.PENDING))
            {
                pendingInvites.add(event);
            }
        }

        return pendingInvites;
    }



    public void sendInvite(String eventId, String invitee) throws ParseException
    {
        String fieldKey = redisKey + ":" + eventId;
        EventModel event = deserialiseEvent(mapRepo.get(redisKey, fieldKey).toString());

        event.getInviteStatus().put(invitee, InviteStatus.PENDING);
        event.getParticipants().add(invitee);

        mapRepo.put(redisKey, fieldKey, serialiseEvent(event));
    }



    public void acceptInvite(String eventId, String username) throws ParseException
    {
        String fieldKey = redisKey + ":" + eventId;
        EventModel event = deserialiseEvent(mapRepo.get(redisKey, fieldKey).toString());

        event.getInviteStatus().put(username, InviteStatus.ACCEPTED);

        mapRepo.put(redisKey, fieldKey, serialiseEvent(event));
    }



    public void rejectInvite(String eventId, String username) throws ParseException
    {
        String fieldKey = redisKey + ":" + eventId;
        EventModel event = deserialiseEvent(mapRepo.get(redisKey, fieldKey).toString());

        event.getInviteStatus().put(username, InviteStatus.REJECTED);

        mapRepo.put(redisKey, fieldKey, serialiseEvent(event));
    }

}
