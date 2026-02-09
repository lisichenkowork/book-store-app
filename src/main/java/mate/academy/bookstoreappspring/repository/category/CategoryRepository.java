package mate.academy.bookstoreappspring.repository.category;

import mate.academy.bookstoreappspring.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category getCategoriesById(Long id);
}
