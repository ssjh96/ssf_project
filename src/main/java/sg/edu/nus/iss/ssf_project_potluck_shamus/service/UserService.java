package sg.edu.nus.iss.ssf_project_potluck_shamus.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.ssf_project_potluck_shamus.constant.Constant;
import sg.edu.nus.iss.ssf_project_potluck_shamus.model.UserModel;
import sg.edu.nus.iss.ssf_project_potluck_shamus.repository.MapRepo;

@Service
public class UserService {

    @Autowired
    private MapRepo mapRepo;

    // storing in memory
    // private static List<UserModel> users = new ArrayList<>();

    public void register(UserModel user)
    {
        String redisKey = Constant.usersKey + ":" + user.getUsername();
        
        mapRepo.put(redisKey, "id", user.getId());
        mapRepo.put(redisKey, "role", user.getRole());
        mapRepo.put(redisKey, "email", user.getEmail());
        mapRepo.put(redisKey, "username", user.getUsername());

        String hashedPw = new BCryptPasswordEncoder().encode(user.getPassword());
        mapRepo.put(redisKey, "password", hashedPw);
    }
    
    // Find user by username
    public UserModel findUsername(String usernameKey)
    {
        String redisKey = Constant.usersKey + ":" + usernameKey;

        String id = mapRepo.get(redisKey, "id").toString();
        String role = mapRepo.get(redisKey, "role").toString();
        String email = mapRepo.get(redisKey, "email").toString();
        String username = mapRepo.get(redisKey, "username").toString();
        String password = mapRepo.get(redisKey, "password").toString();
        
        if (id == null || role == null || email == null || username == null || password == null)
        {
            return null;
        }
        else 
        {
            UserModel user = new UserModel(id, role, email, username, password);
            
            return user;
        }
    }
}
