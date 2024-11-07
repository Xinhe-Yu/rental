package com.chatop.rental.configuration;

import com.chatop.rental.services.CustomUserDetailsService;
import com.chatop.rental.services.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

  private final JWTService jwtService;
  private final CustomUserDetailsService customUserDetailsService;

  public JWTAuthenticationFilter(JWTService jwtService, CustomUserDetailsService customUserDetailsService) {
    this.jwtService = jwtService;
    this.customUserDetailsService = customUserDetailsService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String path = request.getRequestURI();
    if (path.startsWith("/auth/register") || path.startsWith("/auth/login")) {
      filterChain.doFilter(request, response);
      return;
    }
    String token = extractToken(request);
    if (token != null && jwtService.isValidToken(token)) {
      String email = jwtService.extractEmail(token);
      CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
      Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
          userDetails.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    filterChain.doFilter(request, response);
  }

  private String extractToken(HttpServletRequest request) {
    String header = request.getHeader("Authorization");
    if (header != null && header.startsWith("Bearer ")) {
      return header.substring(7);
    }
    return null;
  }
}
