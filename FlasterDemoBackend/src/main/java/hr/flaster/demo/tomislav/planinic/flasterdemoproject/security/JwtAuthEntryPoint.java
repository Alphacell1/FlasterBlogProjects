package hr.flaster.demo.tomislav.planinic.flasterdemoproject.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;

/**
 * Handles unauthorized access by returning a 401 Unauthorized response.
 */
@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    /**
     * Responds with HTTP 401 Unauthorized when an unauthenticated request is made to a protected endpoint.
     *
     * @param request The HTTP request that triggered the authentication failure.
     * @param response The HTTP response to be sent.
     * @param authException The exception thrown due to authentication failure.
     * @throws IOException If an input or output error occurs.
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
    }
}
