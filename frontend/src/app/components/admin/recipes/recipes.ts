import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ApiService, Recipe } from '../../../services/api';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-recipes',
  imports: [FormsModule, RouterLink],
  templateUrl: './recipes.html',
  styleUrl: './recipes.css',
})
export class Recipes implements OnInit {
  items = signal<Recipe[]>([]);
  loading = signal(true);
  isNewItem = signal(false);
  formItem: Recipe = { name: '', instructions: '' };

  constructor(private apiService: ApiService) {}

  ngOnInit(): void {
    this.loadRecipes();
  }

  loadRecipes(): void {
    this.loading.set(true);
    this.apiService.getRecipes().subscribe({
      next: (data) => { this.items.set(data); this.loading.set(false); },
      error: () => { this.items.set([]); this.loading.set(false); }
    });
  }

  newItem(): void {
    this.formItem = { name: '', instructions: '' };
    this.isNewItem.set(true);
  }

  selectItem(item: Recipe): void {
    this.formItem = { ...item };
    this.isNewItem.set(false);
  }

  saveItem(): void {
    if (!this.formItem.name) return;
    if (this.isNewItem()) {
      this.apiService.createRecipe(this.formItem).subscribe({
        next: () => { this.loadRecipes(); this.newItem(); }
      });
    } else {
      if (!this.formItem.id) return;
      this.apiService.updateRecipe(this.formItem.id, this.formItem).subscribe({
        next: () => { this.loadRecipes(); this.newItem(); }
      });
    }
  }

  deleteItem(id: number): void {
    this.apiService.deleteRecipe(id).subscribe({
      next: () => { this.loadRecipes(); this.newItem(); }
    });
  }
}
