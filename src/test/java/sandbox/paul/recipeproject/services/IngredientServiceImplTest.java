package sandbox.paul.recipeproject.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sandbox.paul.recipeproject.commands.IngredientCommand;
import sandbox.paul.recipeproject.converters.IngredientCommandToIngredient;
import sandbox.paul.recipeproject.converters.IngredientToIngredientCommand;
import sandbox.paul.recipeproject.converters.UnitOfMeasureCommandToUnitOfMeasure;
import sandbox.paul.recipeproject.converters.UnitOfMeasureToUnitOfMeasureCommand;
import sandbox.paul.recipeproject.domain.Ingredient;
import sandbox.paul.recipeproject.domain.Recipe;
import sandbox.paul.recipeproject.repositories.RecipeRepository;
import sandbox.paul.recipeproject.repositories.UnitOfMeasureRepository;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class IngredientServiceImplTest {
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;

    IngredientServiceImpl ingredientService;

    public IngredientServiceImplTest() {
        ingredientToIngredientCommand = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
        ingredientCommandToIngredient = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
    }


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        ingredientService = new IngredientServiceImpl(ingredientToIngredientCommand, recipeRepository, unitOfMeasureRepository, ingredientCommandToIngredient);
    }

    @Test
    public void findByRecipeIdAndIngredientId() {
    }

    @Test
    public void findByRecipeIdHappyPath() throws Exception {
        //given
        Recipe recipe = new Recipe();
        recipe.setId(1L);

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(1L);

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(2L);

        recipe.addIngredients(ingredient1);
        recipe.addIngredients(ingredient2);

        Optional<Recipe> recipeOptional = Optional.of(recipe);

        //when

        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);
        IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndIngredientId(1L, 2L);

        //then
        assertEquals(Long.valueOf(2L), ingredientCommand.getId());
        assertEquals(Long.valueOf(1L), ingredientCommand.getRecipeId());
        verify(recipeRepository, times(1)).findById(anyLong());

    }

    @Test
    public void saveIngredientCommand() throws Exception{
        Long testId = 3L;
        IngredientCommand command = new IngredientCommand();
        command.setId(testId);
        command.setRecipeId(2L);

        Optional<Recipe> recipeOptional = Optional.of(new Recipe());

        Recipe savedRecipe = new Recipe();
        savedRecipe.addIngredients(new Ingredient());
        savedRecipe.getIngredients().iterator().next().setId(testId);

        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);
        when(recipeRepository.save(any())).thenReturn(savedRecipe);

        IngredientCommand savedIngredient = ingredientService.saveIngredientCommand(command);

        assertEquals(Long.valueOf(testId),savedIngredient.getId());
        verify(recipeRepository, times(1)).findById(anyLong());
        verify(recipeRepository, times(1)).save(any());
    }

    @Test
    public void testDeleteById() {
        Recipe recipe = new Recipe();
        Ingredient ingredient = new Ingredient();
        ingredient.setId(2L);
        recipe.addIngredients(ingredient);
        ingredient.setRecipe(recipe);

        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);

        ingredientService.deleteById(1L, 2L);

        verify(recipeRepository, times(1)).findById(anyLong());
        verify(recipeRepository,times(1)).save(any());
    }
}