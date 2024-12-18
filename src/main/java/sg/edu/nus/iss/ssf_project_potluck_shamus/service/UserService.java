package sg.edu.nus.iss.ssf_project_potluck_shamus.service;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.ssf_project_potluck_shamus.constant.Constant;
import sg.edu.nus.iss.ssf_project_potluck_shamus.model.User;
import sg.edu.nus.iss.ssf_project_potluck_shamus.repository.MapRepo;

@Service
public class UserService {

    @Autowired
    private MapRepo mapRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    String redisKey = Constant.usersKey;


    
    private String serialiseUser(User user) {
        return Json.createObjectBuilder()
                .add("id", user.getId())
                .add("role", user.getRole())
                .add("email", user.getEmail())
                .add("username", user.getUsername())
                .add("password", passwordEncoder.encode(user.getPassword()))
                .build()
                .toString();
    }



    private User deserialiseUser(String userJsonString) throws ParseException
    {
        JsonReader reader = Json.createReader(new StringReader(userJsonString));
        JsonObject jsonObject = reader.readObject();

        // SimpleDateFormat sdf = new SimpleDateFormat("EEE, MM/dd/yyyy");

        return new User(
                jsonObject.getString("id"),
                jsonObject.getString("role"),
                jsonObject.getString("email"),
                jsonObject.getString("username"),
                jsonObject.getString("password")
        );
    }



    public Boolean emailRegistered(User user) throws ParseException
    {
        // Get all users in redis, (k,v) pairs
        Map<Object, Object> allUsers = mapRepo.getAll(redisKey);

        // entry > k = v
        for (Entry<Object, Object> entry : allUsers.entrySet())
        {
            String userJsonString = entry.getValue().toString();
            String existingEmail = deserialiseUser(userJsonString).getEmail();

            if (user.getEmail().equalsIgnoreCase(existingEmail))
            {
                return true; // if user input email is found registered in redis db
            }
            
        }

        return false; // user input email is not registered in redis db
    }



    public Boolean usernameRegistered(User user) throws ParseException
    {
        String fieldKey = redisKey + ":" + user.getUsername(); 

        // Cannot toString() a null, get object first
        Object userJson = mapRepo.get(redisKey, fieldKey);

        // if specified user does not exist return false
        if (userJson == null)
        {
            return false;
        }

        // else get the existing username 
        String userJsonString = userJson.toString();
        String existingUsername = deserialiseUser(userJsonString).getUsername();

        // check if emails are the same irregardless of upper or lower case
        if (user.getUsername().equalsIgnoreCase(existingUsername)) 
        {
            return true;
        }

        return false;
    }
    


    public void register(User user) throws ParseException
    {
        String fieldKey = redisKey + ":" + user.getUsername();
        mapRepo.put(redisKey, fieldKey, serialiseUser(user)); 
    }

    public String suggestUsername(User user)
    {
        Random rand = new Random();
        String suggestion = "";

        do
        {   
            // suggest new name with 3 numbers behind
            suggestion = user.getUsername() + rand.nextInt(100,1000);
        }
        // Check if the suggestion exist in redis
        while (mapRepo.hasField(redisKey, redisKey + ":" + suggestion));

        return suggestion;
        
    }

    public Boolean authenticate(String inputPassword, User user)
    {     
        // check if user input passsword when encoded, matches the encoded pw stored in redis
        return passwordEncoder.matches(inputPassword, user.getPassword()); 
    }

    
}
