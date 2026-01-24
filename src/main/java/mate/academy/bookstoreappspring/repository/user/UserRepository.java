package mate.academy.bookstoreappspring.repository.user;

import java.util.Optional;
import mate.academy.bookstoreappspring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findUserByEmail(String email);
}
