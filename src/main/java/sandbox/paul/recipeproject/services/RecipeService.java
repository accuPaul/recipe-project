package sandbox.paul.recipeproject.services;

import sandbox.paul.recipeproject.commands.RecipeCommand;
import sandbox.paul.recipeproject.domain.Recipe;

import java.util.Set;

public interface RecipeService {

    Set<Recipe> getRecipes();

    Recipe findById(Long id);

    RecipeCommand saveRecipeCommand(RecipeCommand command);

    RecipeCommand findCommandById(Long id);

    void deleteById(Long id);
}
