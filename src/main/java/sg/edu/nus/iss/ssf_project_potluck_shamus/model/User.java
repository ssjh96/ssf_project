package sg.edu.nus.iss.ssf_project_potluck_shamus.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class User {

    @NotBlank(message = "Username must not be blank.")
    @Size(min = 6, max = 32, message = "Username must be 6-32 characters long.")
    private String username;
    
    @NotBlank(message = "Username must not be blank.")
    @Size(min = 6, max = 32, message = "Password must be 6-32 characters long.")
    private String password;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    
}
