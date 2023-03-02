package jp.co.axa.apidemo.repositories;

import jp.co.axa.apidemo.entities.APIUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface APIUsersRepository extends JpaRepository<APIUsers, String> {
    Optional<APIUsers> findByUsername(String username);
}
