package sandbox.paul.recipeproject.services;

import sandbox.paul.recipeproject.domain.Recipe;

import java.util.Set;

public interface RecipeService {

    Set<Recipe> getRecipes();
}
