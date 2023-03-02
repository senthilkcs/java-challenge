package jp.co.axa.apidemo.filter;

import jp.co.axa.apidemo.services.CustomUserDetailsService;
import jp.co.axa.apidemo.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {JwtFilterTest.class})
class JwtFilterTest {
    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CustomUserDetailsService userDetailsService;

    private JwtFilter jwtFilter;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    public void setup() {
        jwtFilter = new JwtFilter();
        jwtUtil = mock(JwtUtil.class);
        userDetailsService = mock(CustomUserDetailsService.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
        SecurityContextHolder.clearContext();
        jwtFilter.setJwtUtil(jwtUtil);
        jwtFilter.setUserDetailsService(userDetailsService);
    }

    @Test
    void testDoFilterInternalWithValidToken() throws ServletException, IOException {
        // Given
        String token = "validToken";
        String username = "testuser";
        UserDetails userDetails = mock(UserDetails.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.getUsernameFromToken(token)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
        when(jwtUtil.validateToken(token, userDetails)).thenReturn(true);

        jwtFilter.doFilterInternal(request, response, filterChain);

        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        assertEquals(userDetails, authentication.getPrincipal());
        assertNull(authentication.getCredentials());
        assertEquals(userDetails.getAuthorities(), authentication.getAuthorities());
    }

    @Test
    void testDoFilterInternalWithInvalidToken() throws ServletException, IOException {

        String token = "invalidToken";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.getUsernameFromToken(token)).thenReturn(null);

        jwtFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testDoFilterInternalNullCase() throws ServletException, IOException {

        when(request.getHeader("Authorization")).thenReturn(null);
        jwtFilter.doFilterInternal(request, response, filterChain);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
