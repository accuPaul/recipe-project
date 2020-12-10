package sandbox.paul.recipeproject.services;

import sandbox.paul.recipeproject.commands.IngredientCommand;

public interface IngredientService {

    IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long id);

    IngredientCommand saveIngredientCommand(IngredientCommand command);

    void deleteByRecipeIdAndId(Long recipeId, Long id);
}
