package hr.flaster.demo.tomislav.planinic.flasterdemoproject.dto;

/**
 * Represents the request payload for user registration.
 */
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String role;

    /**
     * Default constructor.
     */
    public RegisterRequest() {
    }

    /**
     * Constructs a {@code RegisterRequest} with the provided user details.
     *
     * @param username The desired username.
     * @param password The desired password.
     * @param email The user's email address.
     * @param role The assigned role (e.g., "AUTHOR" or "READER").
     */
    public RegisterRequest(String username, String password, String email, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
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

    /**
     * Gets the email address.
     *
     * @return The email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address.
     *
     * @param email The email to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the user's role.
     *
     * @return The assigned role.
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the user's role.
     *
     * @param role The role to set.
     */
    public void setRole(String role) {
        this.role = role;
    }
}
