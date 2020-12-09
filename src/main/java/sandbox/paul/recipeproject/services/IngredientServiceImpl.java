package sandbox.paul.recipeproject.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sandbox.paul.recipeproject.commands.IngredientCommand;
import sandbox.paul.recipeproject.converters.IngredientToIngredientCommand;
import sandbox.paul.recipeproject.domain.Recipe;
import sandbox.paul.recipeproject.repositories.RecipeRepository;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final RecipeRepository recipeRepository;

    public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand, RecipeRepository recipeRepository) {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.recipeRepository = recipeRepository;
    }


    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if (!recipeOptional.isPresent()) {
            //todo implement error handled for id not found
            log.error("Recipe id "+recipeId+" not found");
        }

        Recipe recipe = recipeOptional.get();
        Optional<IngredientCommand> ingredientOptional = recipe.getIngredients().stream()
        .filter(ingredient -> ingredient.getId().equals(ingredientId))
        .map(ingredient -> ingredientToIngredientCommand.convert(ingredient)).findFirst();

        if(!ingredientOptional.isPresent()) {
            //todo implement error handled for id not found
            log.error("Ingredient "+ingredientId+" not found");
        }

        return ingredientOptional.get();

    }

    @Override
    public void deleteByRecipeIdAndId(Long recipeId, Long id) {

    }
}
