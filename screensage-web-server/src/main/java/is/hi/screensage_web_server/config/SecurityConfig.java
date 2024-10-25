package is.hi.screensage_web_server.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;


/**
 * Security configuration class for setting up authentication and authorization.
 * 
 * This class configures the security settings for the application, including
 * the authentication provider, security filter chain, and JWT filter.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Autowired
  private JwtFilter jwtFilter;

  @Autowired
  private UserDetailsService userDetailsService;

  /**
   * Configures the security filter chain for the application.
   *
   * This method sets up the security policies, including disabling CSRF,
   * defining which endpoints are publicly accessible, and specifying
   * that all other requests require authentication. 
   * It also sets the session management policy to stateless and adds the JWT filter.
   *
   * @param http the HttpSecurity object used to configure security settings
   * @return the configured SecurityFilterChain
   * @throws Exception if any configuration errors occur
   */
  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.csrf(customizer -> customizer.disable())
      .authorizeHttpRequests(request -> request
      .requestMatchers("/", "login", "register", "challenge/**").permitAll()
      .anyRequest().authenticated())
      .httpBasic(Customizer.withDefaults())
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
      .build();
  }

  /**
   * Configures the authentication provider for the application.
   *
   * This method creates a DaoAuthenticationProvider, sets the password
   * encoder, and specifies the UserDetailsService to use for loading
   * user-specific data during authentication.
   *
   * @return the configured AuthenticationProvider
   */
  @Bean
  AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(new BCryptPasswordEncoder(10));
    provider.setUserDetailsService(userDetailsService);
    return provider;
  }

  /**
   * Provides the AuthenticationManager for the application.
   *
   * This method retrieves the AuthenticationManager from the provided
   * AuthenticationConfiguration, allowing it to be used for authenticating
   * users.
   *
   * @param config the AuthenticationConfiguration containing the manager
   * @return the configured AuthenticationManager
   * @throws Exception if any configuration errors occur
   */
  @Bean
  AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

}
