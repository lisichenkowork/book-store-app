package mate.academy.bookstoreappspring.repository.role;

import mate.academy.bookstoreappspring.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
