package is.hi.screensage_web_server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import is.hi.screensage_web_server.services.JWTService;
import is.hi.screensage_web_server.services.ScreenSageUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;


/**
 * Filter for validating JSON Web Tokens (JWT) in incoming requests.
 * 
 * This class extends OncePerRequestFilter to ensure that JWT validation
 * occurs for every request. It checks the Authorization header for a
 * Bearer token, extracts the username from the token, and sets the
 * authentication in the security context if the token is valid.
 */
@Component
public class JwtFilter extends OncePerRequestFilter {
  @Autowired
  private JWTService jwtService;

  @Autowired
  ApplicationContext context;

  /**
   * Performs the filtering of requests to validate the JWT.
   *
   * This method checks the Authorization header for a Bearer token,
   * extracts the username from the token, and, if valid, sets the
   * authentication in the security context. It then proceeds with
   * the filter chain.
   *
   * @param request the HTTP request
   * @param response the HTTP response
   * @param filterChain the filter chain to pass the request and response to
   * @throws ServletException if an error occurs during the filter processing
   * @throws IOException if an input or output error occurs
   */
  @Override
  protected void doFilterInternal(
    @NonNull HttpServletRequest request, 
    @NonNull HttpServletResponse response, 
    @NonNull FilterChain filterChain
  ) throws ServletException, IOException {

    String authHeader = request.getHeader("Authorization");
    String token = null;
    String username = null;

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      token = authHeader.substring(7);
      username = jwtService.extractUserName(token);
    }

    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = context.getBean(ScreenSageUserDetailsService.class).loadUserByUsername(username);
      if (jwtService.validateToken(token, userDetails)) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }

    filterChain.doFilter(request, response);
  }
}
