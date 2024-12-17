package sg.edu.nus.iss.ssf_project_potluck_shamus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.ssf_project_potluck_shamus.constant.Constant;
import sg.edu.nus.iss.ssf_project_potluck_shamus.model.User;
import sg.edu.nus.iss.ssf_project_potluck_shamus.repository.MapRepo;

@Service
public class UserService {

    @Autowired
    private MapRepo mapRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // storing in memory
    // private static List<UserModel> users = new ArrayList<>();

    public void register(User user)
    {
        String redisKey = Constant.usersKey + ":" + user.getUsername();
        
        mapRepo.put(redisKey, "id", user.getId());
        mapRepo.put(redisKey, "role", user.getRole());
        mapRepo.put(redisKey, "email", user.getEmail());
        mapRepo.put(redisKey, "username", user.getUsername());

        String hashedPw = passwordEncoder.encode(user.getPassword());
        mapRepo.put(redisKey, "password", hashedPw);
    }
    
    // Find user by username (For logging in)
    public User findUsername(String usernameKey)
    {
        String redisKey = Constant.usersKey + ":" + usernameKey;

        String username = mapRepo.get(redisKey, "username").toString();

        if (username == null) // if username does not exist
        {
            return null;
        }

        String id = mapRepo.get(redisKey, "id").toString();
        String role = mapRepo.get(redisKey, "role").toString();
        String email = mapRepo.get(redisKey, "email").toString();
        String password = mapRepo.get(redisKey, "password").toString();
        
        User user = new User(id, role, email, username, password);

        return user;
    }

    public Boolean authenticate(String inputPassword, User user)
    {     
        // check if user input passsword when encoded, matches the hashedPw set on creation
        return passwordEncoder.matches(inputPassword, user.getPassword()); 
    }
}
