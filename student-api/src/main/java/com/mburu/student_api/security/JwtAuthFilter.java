package com.mburu.student_api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.HttpServletRequest;
import jakarta.servlet.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userDetails.UserDetails;
import org.springframework.security.core.userDetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServeletResponse response,
            @NonNull FilterChain filterChain) throws ServletException,IOException {
        final String authHeader = request.getAuthHeader("Authorization");

        //no token/doesnt start witn Bearer - let it thru
        if(authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.subString(7); //we strip the Bearer from the authHeader
        final String userEmail = jwtService.extractUsername(jwt);

        //only auth if there is no on=ther session active
        if(userEmail != null && securityContextHolder.getContext().getAuthentication() ==null) {

            UserDetails userDetails = userDetailsService.loadUsersByUsername(userEmail);

            if(jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticatoinToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //responsible for making the request authenticated
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request,response);

    }
    )
}