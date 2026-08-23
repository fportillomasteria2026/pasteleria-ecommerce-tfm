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
  showDeleteConfirm = signal(false);
  deleteTargetId: number | null = null;
  searchQuery = '';
  formItem: Recipe = this.getEmptyItem();

  categorias = ['Bizcocho', 'Crema', 'Cobertura', 'Relleno', 'Decoración', 'General'];
  dificultades = ['Fácil', 'Media', 'Difícil'];

  constructor(private apiService: ApiService) {}

  ngOnInit(): void {
    this.loadRecipes();
  }

  getEmptyItem(): Recipe {
    return {
      name: '',
      instructions: '',
      tartaName: '',
      category: 'General',
      portions: 12,
      prepTimeMinutes: 30,
      cookTimeMinutes: 45,
      difficulty: 'Media',
      ingredients: '',
      notes: '',
      active: true
    };
  }

  loadRecipes(): void {
    this.loading.set(true);
    this.apiService.getRecipes().subscribe({
      next: (data) => { this.items.set(data); this.loading.set(false); },
      error: () => { this.items.set([]); this.loading.set(false); }
    });
  }

  search(): void {
    if (!this.searchQuery.trim()) {
      this.loadRecipes();
      return;
    }
    this.loading.set(true);
    this.apiService.getRecipes().subscribe({
      next: (data) => {
        const q = this.searchQuery.toLowerCase();
        this.items.set(data.filter(r =>
          r.name?.toLowerCase().includes(q) ||
          r.tartaName?.toLowerCase().includes(q) ||
          r.category?.toLowerCase().includes(q)
        ));
        this.loading.set(false);
      },
      error: () => { this.items.set([]); this.loading.set(false); }
    });
  }

  newItem(): void {
    this.formItem = this.getEmptyItem();
    this.isNewItem.set(true);
  }

  selectItem(item: Recipe): void {
    this.formItem = { ...item };
    this.isNewItem.set(false);
  }

  saveItem(): void {
    if (!this.formItem.name?.trim()) return;
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

  confirmDelete(id: number): void {
    this.deleteTargetId = id;
    this.showDeleteConfirm.set(true);
  }

  cancelDelete(): void {
    this.showDeleteConfirm.set(false);
    this.deleteTargetId = null;
  }

  executeDelete(): void {
    if (this.deleteTargetId === null) return;
    this.apiService.deleteRecipe(this.deleteTargetId).subscribe({
      next: () => {
        this.loadRecipes();
        this.showDeleteConfirm.set(false);
        this.deleteTargetId = null;
      }
    });
  }

  getDifficultyColor(d: string): string {
    switch (d) {
      case 'Fácil': return '#22C55E';
      case 'Media': return '#F59E0B';
      case 'Difícil': return '#DC2626';
      default: return '#8B7355';
    }
  }

  getDifficultyBg(d: string): string {
    switch (d) {
      case 'Fácil': return '#DCFCE7';
      case 'Media': return '#FEF3C7';
      case 'Difícil': return '#FEE2E2';
      default: return '#F0E6D6';
    }
  }
}
