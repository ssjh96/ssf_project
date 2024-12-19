package sg.edu.nus.iss.ssf_project_potluck_shamus.service;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.ssf_project_potluck_shamus.model.EventModel;
import sg.edu.nus.iss.ssf_project_potluck_shamus.repository.MapRepo;

@Service
public class EventService 
{
    @Autowired
    private MapRepo mapRepo;

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

        for (Entry<String, String> entry : event.getInviteStatus().entrySet())
        {
            String participant = entry.getKey();
            String inviteStatus = entry.getValue();

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

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String id = jsonObject.getString("id");
        String host = jsonObject.getString("host");

        JsonArray jParticipantsArray = jsonObject.getJsonArray("participants");
        List<String> participants = jParticipantsArray


        // return new EventModel(
        //         jsonObject.getString("id"),
        //         jsonObject.getString("host"),
        //         jsonObject.getJsonArray("participants"), 
        //         jsonObject.getJsonObject("inviteStatus"), 
        //         jsonObject.get, null, eventJsonString)


        // return new EventModel(
        //         jsonObject.getString("id"),
        //         jsonObject.getString("role"),
        //         jsonObject.getString("email"),
        //         jsonObject.getString("username"),
        //         jsonObject.getString("password")
        // );
    }

}
