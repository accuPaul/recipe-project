package sandbox.paul.recipeproject.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sandbox.paul.recipeproject.commands.IngredientCommand;
import sandbox.paul.recipeproject.converters.IngredientCommandToIngredient;
import sandbox.paul.recipeproject.converters.IngredientToIngredientCommand;
import sandbox.paul.recipeproject.domain.Ingredient;
import sandbox.paul.recipeproject.domain.Recipe;
import sandbox.paul.recipeproject.repositories.RecipeRepository;
import sandbox.paul.recipeproject.repositories.UnitOfMeasureRepository;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;

    public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand,
                                 RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository,
                                 IngredientCommandToIngredient ingredientCommandToIngredient) {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
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
    @Transactional
    public IngredientCommand saveIngredientCommand(IngredientCommand command) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(command.getRecipeId());

                if (!recipeOptional.isPresent())
                {
                    log.error("Recipe not found for "+command.getRecipeId());
                    return new IngredientCommand();
                }
                else
                {
                    Optional<Ingredient> ingredientOptional = recipeOptional.get().getIngredients()
                            .stream()
                            .filter(ingredient -> ingredient.getId().equals(command.getId()))
                            .findFirst();

                    if (ingredientOptional.isPresent()){
                        Ingredient ingredientFound = ingredientOptional.get();
                        ingredientFound.setDescription(command.getDescription());
                        ingredientFound.setAmount(command.getAmount());
                        ingredientFound.setUom(unitOfMeasureRepository
                                .findById(command.getUom().getId())
                                .orElseThrow(() -> new RuntimeException("Uom Not Found")));
                    } else {
                        // add new Ingredient
                        Ingredient ingredient = ingredientCommandToIngredient.convert(command);
                        ingredient.setRecipe(recipeOptional.get());
                        recipeOptional.get().addIngredients(ingredient);
                    }
                }

                Recipe savedRecipe = recipeRepository.save(recipeOptional.get());

                Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
                        .filter(recipeIngredients -> recipeIngredients.getId().equals(command.getId()))
                        .findFirst();

                // If we did not find it by ID, let's try by description instead...
                if (!savedIngredientOptional.isPresent()) {
                    savedIngredientOptional = savedRecipe.getIngredients().stream()
                            .filter(recipeIngredients -> recipeIngredients.getDescription().equals(command.getDescription()))
                            .filter(recipeIngredients -> recipeIngredients.getAmount().equals(command.getAmount()))
                            .filter(recipeIngredients -> recipeIngredients.getUom().getId().equals(command.getUom().getId()))
                            .findFirst();
                }
;
//todo check for failure to find ingredients
        return ingredientToIngredientCommand.convert(savedIngredientOptional.get());
    }


    @Override
    public void deleteById(Long recipeId, Long ingredientId) {
        // First make sure we have the recipe in question...
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if(recipeOptional.isPresent()) {
            Recipe recipe = recipeOptional.get();

            Optional<Ingredient> ingredientOptional = recipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(ingredientId))
                    .findFirst();

            if (ingredientOptional.isPresent()){
                ingredientOptional.get().setRecipe(null);
                recipe.getIngredients().remove(ingredientOptional.get());
                recipeRepository.save(recipe);
            }

        } else {
            log.error("No Recipe matches id "+recipeId);
        }

    }
}
