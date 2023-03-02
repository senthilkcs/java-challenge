package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.entities.JwtRequest;
import jp.co.axa.apidemo.entities.JwtResponse;
import jp.co.axa.apidemo.services.CustomUserDetailsService;
import jp.co.axa.apidemo.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = {AuthControllerTest.class})
class AuthControllerTest {
    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void testGenerateTokenWithValidCredentials() throws Exception {

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);

        String mockJwtToken = "mockJwtToken";
        when(jwtUtil.generateToken(userDetails)).thenReturn(mockJwtToken);

        JwtRequest jwtRequest = new JwtRequest("testuser", "password");

        JwtResponse jwtResponse = authController.generateToken(jwtRequest);
        assertNotNull(jwtResponse);
        assertTrue(jwtResponse.getJwtToken().contains("Bearer " + mockJwtToken));
    }
}

