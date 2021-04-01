package authenticationsystem;

/**
 *
 * @author Colton Thompson <colton.thompson1@snhu.edu>
 * Date: 08/14/19
 * 
 */
public class User {
 
    private String username;
    
    private String hash;
    
    private String password;
    
    private UserRole role;
    
    public User(String username, String hash, String password, UserRole role) {
        this.username = username;
        this.hash = hash;
        this.password = password;
        this.role = role;
    }
    
    public String toString() {
        return "username: " + username + ", hash: " + hash + ", password: " + password + ", role: " + role.toString();
    }

    public String getUsername() {
        return username;
    }
    
    public String getHash() {
        return hash;
    }
    
    public String getPassword() {
        return password;
    }
    
    public UserRole getRole() {
        return role;
    }
}
