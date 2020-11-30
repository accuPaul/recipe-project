package sandbox.paul.recipeproject.repositories;

import org.springframework.data.repository.CrudRepository;
import sandbox.paul.recipeproject.domain.Recipe;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {
}
