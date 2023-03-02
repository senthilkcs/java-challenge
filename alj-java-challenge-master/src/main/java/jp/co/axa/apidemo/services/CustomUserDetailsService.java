package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.APIUsers;
import jp.co.axa.apidemo.repositories.APIUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private APIUsersRepository usersRepository;

    public void setUsersRepository(APIUsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    /**
     * Load user details by username from the APIUsers repository and create a UserDetailsImpl object.
     * @return a UserDetailsImpl object containing the user's details if found, null otherwise
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        APIUsers apiUsers = usersRepository.findByUsername(username).orElse(new APIUsers());
        return new UserDetailsImpl(apiUsers);
    }
}
