package com.mburu.student_api.security;

import com.mburu.student_api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.secuirty.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetials.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserReposioty userReposioty;
    private final JwtAuthFilter jwtAuthFilter;

    //loads a user by email when spring security needs to auth
    @Bean
    public UserDetailsService userDetailsService(){
        return  email -> userReposioty.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found:" + email));
    }

    //using BCrypt for hashing the passwords
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //connects UserDetailsService + password encoder for login checks
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(passwordEncoder());
        provider.setUserDetailsService(userDetailsService());
        return provider;
    }

    //used by AuthContoller to perform actual login check
    @Bean
    public AuthenticationManager authenticationManager() {
        return config.getAuthenticatioManager();
    }

    //MAIN Security rules

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                //public endpoint are not protected(no auth required)
                        .requestMatchers("/api/auth/**").permitAll()
                        // read access -  any auth user
                        .requestMatchers(HttpMethod.GET, "/api/students/**").hasAnyRole("ADMIN", "USER")
                                //write access - admins only
                                .requestMatcher(HttpMethod.POST, "/api/students**").hasRole("ADMIN")
                                .requestMatcher(HttpMethod.PUT, "/api/students**").hasRole("ADMIN")
                                .requestMatcher(HttpMethod.DELETE, "/api/students**").hasRole("ADMIN")

                                // every other request requires authentication
                                .anyRequest().authenticated()

                )
                .authenticationProvider(authenticationProvider())
                //insert JWT filer BEFORE spring's default auth filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

        return http.build();
    }

}
