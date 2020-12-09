package sandbox.paul.recipeproject.services;

import org.springframework.stereotype.Service;
import sandbox.paul.recipeproject.commands.IngredientCommand;

@Service
public interface IngredientService {

    IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long id);

    void deleteByRecipeIdAndId(Long recipeId, Long id);
}
