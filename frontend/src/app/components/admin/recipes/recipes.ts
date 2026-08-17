import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ApiService, Recipe } from '../../../services/api';

@Component({
  selector: 'app-recipes',
  imports: [FormsModule],
  templateUrl: './recipes.html',
  styleUrl: './recipes.css',
})
export class Recipes implements OnInit {
  recipes = signal<Recipe[]>([]);
  loading = signal(true);
  newRecipe: Recipe = { name: '', instructions: '' };
  editingRecipe = signal<Recipe | null>(null);

  constructor(private apiService: ApiService) {}

  ngOnInit(): void {
    this.loadRecipes();
  }

  loadRecipes(): void {
    this.loading.set(true);
    this.apiService.getRecipes().subscribe({
      next: (data) => { this.recipes.set(data); this.loading.set(false); },
      error: () => { this.recipes.set([]); this.loading.set(false); }
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
