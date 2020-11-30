package sandbox.paul.recipeproject.repositories;

import org.springframework.data.repository.CrudRepository;
import sandbox.paul.recipeproject.domain.Category;

public interface CategoryRepository extends CrudRepository<Category, Long> {
}
