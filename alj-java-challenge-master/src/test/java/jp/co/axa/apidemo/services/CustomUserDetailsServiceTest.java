package jp.co.axa.apidemo.services;


import jp.co.axa.apidemo.entities.APIUsers;
import jp.co.axa.apidemo.repositories.APIUsersRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {CustomUserDetailsServiceTest.class})
class CustomUserDetailsServiceTest {

    @Mock
    private APIUsersRepository usersRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    @Test
    void testLoadUserByUsernameWhenUserExists() {

        APIUsers apiUsers = new APIUsers();
        apiUsers.setUsername("testuser");
        apiUsers.setPassword("password");
        apiUsers.setEnabled(true);
        apiUsers.setRole("ADMIN");

        when(usersRepository.findByUsername("testuser")).thenReturn(Optional.of(apiUsers));

        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");

        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertEquals(1, userDetails.getAuthorities().size());
        assertEquals("ROLE_ADMIN", userDetails.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void testLoadUserByUsernameWhenUserDoesNotExist() {
        when(usersRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");
        assertEquals(null, userDetails.getUsername());
        assertEquals(null, userDetails.getPassword());
        assertFalse(userDetails.isEnabled());
    }
}

