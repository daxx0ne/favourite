package yu.favourite.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yu.favourite.user.entity.SiteUser;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
    Optional<SiteUser> findByUsername(String username);
}