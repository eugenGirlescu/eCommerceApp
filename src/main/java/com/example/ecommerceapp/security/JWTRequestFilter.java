package com.example.ecommerceapp.security;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.example.ecommerceapp.dto.UserGetDTO;
import com.example.ecommerceapp.model.User;
import com.example.ecommerceapp.repository.UserRepository;
import com.example.ecommerceapp.service.JWTService;
import com.example.ecommerceapp.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JWTRequestFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final UserRepository userRepository;
    private final UserService userService;
    private static final Integer SKIP_BEARER = 7;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenHeader = request.getHeader("Authorization");
        if (tokenHeader != null && tokenHeader.startsWith("Bearer")) {
            String token = tokenHeader.substring(SKIP_BEARER);
            try {
                String userName = jwtService.getUserName(token);
                Optional<User> userFromDb = userRepository.findByUserNameIgnoreCase(userName);
                if(userFromDb.isPresent()) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userFromDb.get(), null, new ArrayList());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (JWTDecodeException exception) {

            }
        }
        filterChain.doFilter(request, response);
    }
}
