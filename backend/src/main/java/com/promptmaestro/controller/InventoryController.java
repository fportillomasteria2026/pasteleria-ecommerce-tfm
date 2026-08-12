package com.promptmaestro.controller;

import com.promptmaestro.entity.Ingredient;
import com.promptmaestro.entity.Recipe;
import com.promptmaestro.repository.IngredientRepository;
import com.promptmaestro.repository.RecipeRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class InventoryController {

    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;

    public InventoryController(IngredientRepository ingredientRepository, RecipeRepository recipeRepository) {
        this.ingredientRepository = ingredientRepository;
        this.recipeRepository = recipeRepository;
    }

    @GetMapping("/ingredients")
    public ResponseEntity<List<Ingredient>> getAllIngredients(
            @RequestParam(required = false) String search) {
        if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(ingredientRepository.findByNameContainingIgnoreCase(search));
        }
        return ResponseEntity.ok(ingredientRepository.findAll());
    }

    @PostMapping("/ingredients")
    public ResponseEntity<Ingredient> createIngredient(@Valid @RequestBody Ingredient ingredient) {
        return ResponseEntity.ok(ingredientRepository.save(ingredient));
    }

    @PutMapping("/ingredients/{id}")
    public ResponseEntity<Ingredient> updateIngredient(@PathVariable Long id,
                                                        @Valid @RequestBody Ingredient updated) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));
        ingredient.setName(updated.getName());
        ingredient.setStockQuantity(updated.getStockQuantity());
        ingredient.setUnit(updated.getUnit());
        return ResponseEntity.ok(ingredientRepository.save(ingredient));
    }

    @DeleteMapping("/ingredients/{id}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Long id) {
        ingredientRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/recipes")
    public ResponseEntity<List<Recipe>> getAllRecipes(
            @RequestParam(required = false) String search) {
        if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(recipeRepository.findByNameContainingIgnoreCase(search));
        }
        return ResponseEntity.ok(recipeRepository.findAll());
    }

    @PostMapping("/recipes")
    public ResponseEntity<Recipe> createRecipe(@Valid @RequestBody Recipe recipe) {
        return ResponseEntity.ok(recipeRepository.save(recipe));
    }

    @PutMapping("/recipes/{id}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable Long id,
                                                @Valid @RequestBody Recipe updated) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
        recipe.setName(updated.getName());
        recipe.setInstructions(updated.getInstructions());
        return ResponseEntity.ok(recipeRepository.save(recipe));
    }

    @DeleteMapping("/recipes/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        recipeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
