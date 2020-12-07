package sandbox.paul.recipeproject.controllers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import sandbox.paul.recipeproject.commands.RecipeCommand;
import sandbox.paul.recipeproject.domain.Recipe;
import sandbox.paul.recipeproject.services.RecipeService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class RecipeControllerTest {

    @Mock
    RecipeService recipeService;

    RecipeController recipeController;
    MockMvc mockMvc;

    @Before
    public void SetUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        recipeController= new RecipeController(recipeService);
        mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();
    }

    @Test
    public void testGetRecipe() throws Exception {
        Long testId = 1L;

        Recipe recipe =new Recipe();
        recipe.setId(testId);

        when(recipeService.findById(anyLong())).thenReturn(recipe);

        mockMvc.perform(get("/recipe/"+testId.toString()+"/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/show"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    public void testGetNewRecipeForm() throws Exception {
        RecipeCommand command = new RecipeCommand();

        mockMvc.perform(get("/recipe/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeform"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    public void testPostNewRecipeForm() throws Exception {
        Long testId = 5L;
        RecipeCommand command = new RecipeCommand();
        command.setId(testId);

        when(recipeService.saveRecipeCommand(any())).thenReturn(command);

        mockMvc.perform(post("/recipe")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param("id", "")
        .param("description", "a test string")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/"+command.getId().toString()+"/show"));
    }

    @Test
    public void testGetUpdateView() throws Exception {
        Long testId = 5L;
        RecipeCommand command = new RecipeCommand();
        command.setId(testId);

        when(recipeService.findCommandById(anyLong())).thenReturn(command);

        mockMvc.perform(get("/recipe/"+command.getId().toString()+"/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeform"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    public void testDeleteAction() throws Exception {
        mockMvc.perform(get("/recipe/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        verify(recipeService,times(1)).deleteById(anyLong());
    }
}