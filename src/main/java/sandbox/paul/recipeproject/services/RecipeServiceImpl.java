package sandbox.paul.recipeproject.services;

import org.springframework.stereotype.Service;
import sandbox.paul.recipeproject.domain.Recipe;
import sandbox.paul.recipeproject.repositories.RecipeRepository;

import java.util.HashSet;
import java.util.Set;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public  Set<Recipe> getRecipes() {
        //log.debug("This is a Lomboc debugger");
        Set<Recipe> recipes = new HashSet<>();

        recipeRepository.findAll().iterator().forEachRemaining(recipes::add);
        return recipes;
    }
}
