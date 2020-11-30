package sandbox.paul.recipeproject.repositories;

import org.springframework.data.repository.CrudRepository;
import sandbox.paul.recipeproject.domain.UnitOfMeasure;

public interface UnitOfMeasureRepository extends CrudRepository<UnitOfMeasure, Long> {
}
