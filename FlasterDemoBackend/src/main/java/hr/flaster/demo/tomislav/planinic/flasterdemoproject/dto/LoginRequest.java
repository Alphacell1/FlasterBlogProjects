package hr.flaster.demo.tomislav.planinic.flasterdemoproject.dto;

/**
 * Represents the request payload for user login.
 */
public class LoginRequest {
    private String username;
    private String password;

    /**
     * Default constructor.
     */
    public LoginRequest() {
    }

    /**
     * Constructs a {@code LoginRequest} with the provided username and password.
     *
     * @param username The username of the user attempting to log in.
     * @param password The password of the user attempting to log in.
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Gets the username.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username The username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password.
     *
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password The password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
