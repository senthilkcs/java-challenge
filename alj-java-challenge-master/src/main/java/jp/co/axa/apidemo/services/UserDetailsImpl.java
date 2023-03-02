package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.APIUsers;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Implements the UserDetails interface to provide user details for authentication and authorization.
 */
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private Boolean isActive;
    private List<SimpleGrantedAuthority> authorities;

    public UserDetailsImpl(APIUsers apiUsers) {
        this.username = apiUsers.getUsername();
        this.password = apiUsers.getPassword();
        this.isActive = apiUsers.isEnabled();
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + apiUsers.getRole()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}
