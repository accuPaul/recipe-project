package sandbox.paul.recipeproject.controllers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import sandbox.paul.recipeproject.commands.IngredientCommand;
import sandbox.paul.recipeproject.commands.RecipeCommand;
import sandbox.paul.recipeproject.services.IngredientService;
import sandbox.paul.recipeproject.services.RecipeService;
import sandbox.paul.recipeproject.services.UnitOfMeasureService;

import java.util.HashSet;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class IngredientControllerTest {

    @Mock
    RecipeService recipeService;

    @Mock
    IngredientService ingredientService;

    @Mock
    UnitOfMeasureService unitOfMeasureService;

    IngredientController ingredientController;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        ingredientController = new IngredientController(recipeService, ingredientService,unitOfMeasureService);
        mockMvc = MockMvcBuilders.standaloneSetup(ingredientController).build();
    }

    @Test
    public void testListIngredients() throws Exception {
        //given
        RecipeCommand recipeCommand = new RecipeCommand();
        when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);

        //when
        mockMvc.perform(get("/recipe/1/ingredients"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/list"))
                .andExpect(model().attributeExists("recipe"));
        //then

        verify(recipeService, times(1)).findCommandById(anyLong());
    }


    @Test
    public void showIngredient() throws Exception{
        //given
        IngredientCommand ingredientCommand = new IngredientCommand();
        //when

        when(ingredientService.findByRecipeIdAndIngredientId(anyLong(), anyLong())).thenReturn(ingredientCommand);
        //then

        mockMvc.perform(get("/recipe/1/ingredient/2/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/show"))
                .andExpect(model().attributeExists("ingredient"));
    }

    @Test
    public void updateRecipeIngredient() throws Exception {
        //given
        IngredientCommand ingredientCommand = new IngredientCommand();

        //when
        when(ingredientService.findByRecipeIdAndIngredientId(anyLong(),anyLong())).thenReturn(ingredientCommand);
        when(unitOfMeasureService.listAllUoms()).thenReturn(new HashSet<>());

    //then
        mockMvc.perform(get("/recipe/1/ingredient/2/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/ingredientform"))
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attributeExists("uomList"));
    }

    @Test
    public void testNewIngredient() throws Exception {
        //given -- we need a recipe to which to add this item
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);

        //when -- we need a call against the services to fetch a recipe and or list of UoM's
        when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);
        when(unitOfMeasureService.listAllUoms()).thenReturn(new HashSet<>());

        //then -- make a mock call to the url to see what wackiness ensues, this should yield both a UoM list and an ingredient
        mockMvc.perform(get("/recipe/1/ingredient/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/ingredientform"))
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attributeExists("uomList"));

        verify(recipeService, times(1)).findCommandById(anyLong());
    }

    @Test
    public void saveOrUpdate() throws Exception {
        IngredientCommand command = new IngredientCommand();
        command.setRecipeId(1L);
        command.setId(2L);

        when(ingredientService.saveIngredientCommand(any())).thenReturn(command);

        mockMvc.perform(post("/recipe/1/ingredient")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id","")
                .param("description","a string")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/1/ingredient/2/show"));

    }

    @Test
    public void testDeleteIngredient() throws Exception {
        mockMvc.perform(get("/recipe/2/ingredient/1/delete")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param("id","")
        .param("description","any string")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/2/ingredients"));
    }
}