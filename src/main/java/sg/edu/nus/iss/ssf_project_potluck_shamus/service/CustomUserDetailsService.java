package sg.edu.nus.iss.ssf_project_potluck_shamus.service;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.ssf_project_potluck_shamus.model.UserModel;

@Service
public class CustomUserDetailsService implements UserDetailsService{

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
    {
        UserModel currentUser = new UserModel();

        try 
        {
            currentUser = userService.findUser(username);

            if(currentUser == null)
            {
                return null;
            }

            return User.builder()
                .username(currentUser.getUsername())
                .password(currentUser.getPassword())
                .roles(currentUser.getRole())
                .build();
            
        } 
        catch (ParseException e) 
        {
            e.printStackTrace();
            return null;
        }

        
        
        
        // // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'loadUserByUsername'");
    }
    
}
