package sg.edu.nus.iss.ssf_project_potluck_shamus.model;

import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class User {

    // VALIDATIONS
    // Non-validation Attributes
    private String id;
    private String role;
    
    // Validated Attributes
    @NotBlank(message = "Email field must not be blank.")
    @Email(message = "Please enter a valid email.")
    private String email;

    @NotBlank(message = "Username must not be blank.")
    @Size(min = 6, max = 32, message = "Username must be 6-32 characters long.")
    private String username;
    
    @NotBlank(message = "Password must not be blank.")
    @Size(min = 6, max = 32, message = "Password must be 6-32 characters long.")
    private String password;

    // CONSTRUCTORS
    // Default Constructor for User object
    public User() 
    {
        this.id = UUID.randomUUID().toString().replace("-", "").substring(0,  4);
        this.role = "default_role";
    }
    
    // Constructor for new User object
    public User(String email, String username, String password) 
    {
        this.id = UUID.randomUUID().toString().replace("-", "").substring(0,  4);
        this.role = "default_role";

        this.email = email;
        this.username = username;
        this.password = password;
    }

    // Constructor for updating or rebuilding User object from data stored in Redis DB
    public User(String id, String role, String email, String username, String password) 
    {
        this.id = id;
        this.role = role;

        this.email = email;
        this.username = username;
        this.password = password;
    }

    // GETTERS SETTERS
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    
}
