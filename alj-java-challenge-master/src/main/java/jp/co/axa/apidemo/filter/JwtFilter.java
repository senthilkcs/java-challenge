package jp.co.axa.apidemo.filter;

import jp.co.axa.apidemo.services.CustomUserDetailsService;
import jp.co.axa.apidemo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    /**
     * Utility class for JWT token generation and validation.
     */
    @Autowired
    private JwtUtil jwtUtil;

    public void setJwtUtil(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Service class for loading user details.
     */
    @Autowired
    private CustomUserDetailsService userDetailsService;

    public void setUserDetailsService(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    /**
     * Method for filtering and validating JWT token.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String authorization = httpServletRequest.getHeader("Authorization");
        String token = null;
        String userName = null;

        if (null != authorization && authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);
            userName = jwtUtil.getUsernameFromToken(token);
        }

        if (null != userName && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
            boolean validToken = jwtUtil.validateToken(token, userDetails);
            if (validToken) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
