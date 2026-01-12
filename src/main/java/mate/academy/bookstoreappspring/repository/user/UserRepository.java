package mate.academy.bookstoreappspring.repository.user;

import mate.academy.bookstoreappspring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
