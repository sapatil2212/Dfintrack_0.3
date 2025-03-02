package zenith_expense_tracker_nov_v1.securityConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import zenith_expense_tracker_nov_v1.jwt.JwtAuthenticationEntryPoint;
import zenith_expense_tracker_nov_v1.jwt.JwtAuthenticationFilter;

import java.util.Arrays;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@EnableScheduling
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Value("${frontend.url}")
    private  String frontendUrl;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(frontendUrl)); // Allow frontend URL
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "user-id" , "userId", "adminId"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        // Public endpoints
                        .requestMatchers("api/test/token", "/users/login", "/users/**", "/registration/**",
                                "api/users/**",  "api/expenses/**", "api/booking/**", "api/bills/**", "api/**",
                                "api/property-revenues/**", "api/personal-revenues/**", "api/customers", "api/bookings").permitAll()
                        .requestMatchers("api/profile/**").authenticated()
                        // Admin-specific endpoints

                        .requestMatchers("/api/caretaker/**", "/api/admin/properties/**").permitAll()
                        // Public access for properties (non-admin)
                        .requestMatchers("/properties/**").permitAll()
                        // Any other request requires authentication
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Add JWT filter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
    /*@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(authz -> authz
                        // Allow public access to registration and login
                        .requestMatchers("/users/**").permitAll()
                        .requestMatchers("api/users/**").permitAll()
                        .requestMatchers("/registration/**").permitAll()

                        // Restrict access to caretaker API to ADMIN role
                        .requestMatchers(HttpMethod.GET, "/api/caretaker/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/caretaker/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/caretaker/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/caretaker/**").hasRole("ADMIN")

                        // Restrict access to properties API to ADMIN role
                        .requestMatchers(HttpMethod.GET, "/api/admin/properties/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/admin/properties/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/admin/properties/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/admin/properties/**").hasRole("ADMIN")

                        // Allow public access to properties outside admin endpoints
                        .requestMatchers(HttpMethod.GET, "/properties/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/properties/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/properties/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/properties/**").permitAll()

                        // Protect all other endpoints (require authentication)
                        .anyRequest().authenticated()
                )
                // Use stateless sessions (suitable for APIs)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }*/


