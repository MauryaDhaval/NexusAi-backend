package com.dhaval.nexusai.security;

import com.dhaval.nexusai.entity.ApiKey;
import com.dhaval.nexusai.entity.types.ApiKeyStatus;
import com.dhaval.nexusai.service.ApiKeyService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ApiKeyFilter  extends OncePerRequestFilter {

    final private CustomUserDetailsService userDetailsService;
    final private ApiKeyService apiKeyService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String apikey = request.getHeader("x-api-key");
        if(apikey != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            ApiKey key = apiKeyService.findByKey(apikey);
            request.setAttribute("apiKey" , key);
            if( key != null && key.getStatus() == ApiKeyStatus.ACTIVE){
                UserDetails userDetails = userDetailsService.loadUserByUsername(key.getUser().getEmail());
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails , null ,userDetails.getAuthorities());
                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().startsWith("/chat");
    }

}
