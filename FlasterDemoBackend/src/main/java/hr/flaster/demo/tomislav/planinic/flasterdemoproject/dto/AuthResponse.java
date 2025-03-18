package hr.flaster.demo.tomislav.planinic.flasterdemoproject.dto;

/**
 * Represents the response returned after a successful authentication request.
 */
public class AuthResponse {
    private String token;
    private String message;

    /**
     * Default constructor.
     */
    public AuthResponse() {
    }

    /**
     * Constructs an {@code AuthResponse} with a token and a message.
     *
     * @param token  The JWT token assigned to the authenticated user.
     * @param message A message indicating the authentication status.
     */
    public AuthResponse(String token, String message) {
        this.token = token;
        this.message = message;
    }

    /**
     * Gets the authentication token.
     *
     * @return The JWT token.
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the authentication token.
     *
     * @param token The JWT token to set.
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Gets the authentication message.
     *
     * @return The authentication status message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the authentication message.
     *
     * @param message The message to set.
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
