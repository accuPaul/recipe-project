package sandbox.paul.recipeproject.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sandbox.paul.recipeproject.commands.IngredientCommand;
import sandbox.paul.recipeproject.services.IngredientService;
import sandbox.paul.recipeproject.services.RecipeService;
import sandbox.paul.recipeproject.services.UnitOfMeasureService;

@Slf4j
@Controller
public class IngredientController {
    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;

    public IngredientController(RecipeService recipeService, IngredientService ingredientService,
                                UnitOfMeasureService unitOfMeasureService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @GetMapping
    @RequestMapping("recipe/{recipeId}/ingredients")
    public String listIngredients(@PathVariable String recipeId, Model model){
        log.debug("Getting ingredient list for recipeId "+recipeId);
        model.addAttribute("recipe", recipeService.findCommandById(Long.valueOf(recipeId)));
        return "recipe/ingredient/list";
    }

    @GetMapping
    @RequestMapping("recipe/{recipeId}/ingredient/{ingredientId}/show")
    public String showIngredient(@PathVariable String recipeId, @PathVariable String ingredientId, Model model)
    {
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId),
                Long.valueOf(ingredientId)));
        return "recipe/ingredient/show";
    }

    @GetMapping
    @RequestMapping("recipe/{recipeId}/ingredient/{ingredientId}/update")
    public String updateRecipeIngredient(@PathVariable String recipeId, @PathVariable String ingredientId, Model model)
    {
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId),
                Long.valueOf(ingredientId)));
        model.addAttribute("uomList", unitOfMeasureService.listAllUoms());
        return "recipe/ingredient/ingredientform";
    }

    @PostMapping
    @RequestMapping("recipe/{recipeId}/ingredient")
    public String saveOrUpdate(@ModelAttribute IngredientCommand command){
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);

        return "redirect:/recipe/"+savedCommand.getRecipeId()+"/ingredient/"+savedCommand.getId()+"/show";
    }
}

