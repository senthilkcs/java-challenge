package jp.co.axa.apidemo.config;

import jp.co.axa.apidemo.filter.JwtFilter;
import jp.co.axa.apidemo.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static jp.co.axa.apidemo.constant.SecurityConstant.*;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Injects a custom UserDetailsService implementation
     */
    @Autowired
    @Qualifier("customUserDetailsService")
    private CustomUserDetailsService customUserDetailsService;

    /**
     * Injects a custom JWT filter
     */
    @Autowired
    private JwtFilter jwtFilter;

    /**
     * Sets the custom UserDetailsService implementation for authentication
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService);
    }

    /**
     * Defines the password encoder used for password hashing
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Creates the authentication manager bean for authentication handling
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * Spring security configurations
     * Specifies which requests should be allowed without authentication
     * Specifies the authorization rules for HTTP methods and paths
     * specifies that Spring Security should not create a session
     * Adds the custom JWT filter to the filter chain
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/h2-console/**", "/v2/api-docs/**", "/swagger-ui.html/**", "/swagger-resources/**", "/webjars/**", "/authenticate")
                .permitAll()
                .antMatchers(HttpMethod.GET, EMPLOYEE_URL).hasAnyRole(ADMIN_ROLE, READ_ROLE, WRITE_ROLE)
                .antMatchers(HttpMethod.POST, EMPLOYEE_URL).hasAnyRole(ADMIN_ROLE, WRITE_ROLE)
                .antMatchers(HttpMethod.PUT, EMPLOYEE_URL).hasAnyRole(ADMIN_ROLE, WRITE_ROLE)
                .antMatchers(HttpMethod.DELETE, EMPLOYEE_URL).hasRole(ADMIN_ROLE)
                .anyRequest()
                .authenticated()
                .and()
                .headers()
                .frameOptions()
                .disable()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
