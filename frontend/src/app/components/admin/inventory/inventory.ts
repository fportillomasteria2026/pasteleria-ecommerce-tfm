import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ApiService, Ingredient, Recipe } from '../../../services/api';

@Component({
  selector: 'app-inventory',
  imports: [FormsModule],
  templateUrl: './inventory.html',
  styleUrl: './inventory.css',
})
export class Inventory implements OnInit {
  ingredients = signal<Ingredient[]>([]);
  recipes = signal<Recipe[]>([]);
  loading = signal(true);
  activeTab = signal<'ingredients' | 'recipes'>('ingredients');

  newIngredient: Ingredient = { name: '', stockQuantity: 0, unit: 'kg' };
  newRecipe: Recipe = { name: '', instructions: '' };

  editingIngredient = signal<Ingredient | null>(null);
  editingRecipe = signal<Recipe | null>(null);

  constructor(private apiService: ApiService) {}

  ngOnInit(): void {
    this.loadIngredients();
    this.loadRecipes();
  }

  loadIngredients(): void {
    this.apiService.getIngredients().subscribe({
      next: (data) => this.ingredients.set(data),
      error: () => this.ingredients.set([])
    });
  }

  loadRecipes(): void {
    this.apiService.getRecipes().subscribe({
      next: (data) => this.recipes.set(data),
      error: () => this.recipes.set([])
    });
    this.loading.set(false);
  }

  addIngredient(): void {
    if (!this.newIngredient.name) return;
    this.apiService.createIngredient(this.newIngredient).subscribe({
      next: (ingredient) => {
        this.ingredients.update(list => [...list, ingredient]);
        this.newIngredient = { name: '', stockQuantity: 0, unit: 'kg' };
      }
    });
  }

  updateIngredient(ingredient: Ingredient): void {
    if (!ingredient.id) return;
    this.apiService.updateIngredient(ingredient.id, ingredient).subscribe({
      next: () => {
        this.editingIngredient.set(null);
        this.loadIngredients();
      }
    });
  }

  deleteIngredient(id: number): void {
    this.apiService.deleteIngredient(id).subscribe({
      next: () => this.loadIngredients()
    });
  }

  addRecipe(): void {
    if (!this.newRecipe.name) return;
    this.apiService.createRecipe(this.newRecipe).subscribe({
      next: (recipe) => {
        this.recipes.update(list => [...list, recipe]);
        this.newRecipe = { name: '', instructions: '' };
      }
    });
  }

  updateRecipe(recipe: Recipe): void {
    if (!recipe.id) return;
    this.apiService.updateRecipe(recipe.id, recipe).subscribe({
      next: () => {
        this.editingRecipe.set(null);
        this.loadRecipes();
      }
    });
  }

  deleteRecipe(id: number): void {
    this.apiService.deleteRecipe(id).subscribe({
      next: () => this.loadRecipes()
    });
  }
}
