package jp.co.axa.apidemo.controllers;

import io.swagger.annotations.Api;
import jp.co.axa.apidemo.entities.JwtRequest;
import jp.co.axa.apidemo.entities.JwtResponse;
import jp.co.axa.apidemo.services.CustomUserDetailsService;
import jp.co.axa.apidemo.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling authentication requests.
 */
@RestController
@RequestMapping("/")
@Api(tags = "JWT Token Generator")
public class AuthController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;

    public void setCustomUserDetailsService(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Generates a JWT token for the given username and password.
     */
    @PostMapping("/authenticate")
    public JwtResponse generateToken(@RequestBody JwtRequest jwtRequest) throws BadCredentialsException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword())
            );
        } catch (AuthenticationException ex) {
            throw new BadCredentialsException("Inavalid username/password", ex);
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getUsername());

        final String token = jwtUtil.generateToken(userDetails);

        LOG.info("JWK token has been generated successfully for given username and password");

        return new JwtResponse("Bearer " + token);
    }
}
