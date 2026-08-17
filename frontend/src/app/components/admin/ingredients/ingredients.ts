import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ApiService, MateriaPrima } from '../../../services/api';

@Component({
  selector: 'app-ingredients',
  imports: [FormsModule],
  templateUrl: './ingredients.html',
  styleUrl: './ingredients.css',
})
export class Ingredients implements OnInit {
  items = signal<MateriaPrima[]>([]);
  loading = signal(true);
  isNewItem = signal(false);
  formItem: MateriaPrima = this.getEmptyItem();

  unidades = ['KG', 'G', 'L', 'ML', 'U'];

  constructor(private apiService: ApiService) {}

  ngOnInit(): void {
    this.loadItems();
  }

  loadItems(): void {
    this.loading.set(true);
    this.apiService.getMateriaPrima().subscribe({
      next: (data) => { this.items.set(data); this.loading.set(false); },
      error: () => { this.items.set([]); this.loading.set(false); }
    });
  }

  getEmptyItem(): MateriaPrima {
    return { nombre: '', marca: '', proveedor: '', coste: 0, formato: '', peso: 0, unidad: 'KG', cantidad: 0 };
  }

  newItem(): void {
    this.formItem = this.getEmptyItem();
    this.isNewItem.set(true);
  }

  selectItem(item: MateriaPrima): void {
    this.formItem = {...item};
    this.isNewItem.set(false);
  }

  saveItem(): void {
    if (!this.formItem.nombre) return;
    if (this.isNewItem()) {
      this.apiService.createMateriaPrima(this.formItem).subscribe({
        next: () => { this.loadItems(); this.newItem(); }
      });
    } else {
      if (!this.formItem.id) return;
      this.apiService.updateMateriaPrima(this.formItem.id, this.formItem).subscribe({
        next: () => { this.loadItems(); this.newItem(); }
      });
    }
  }

  deleteItem(id: number): void {
    this.apiService.deleteMateriaPrima(id).subscribe({
      next: () => { this.loadItems(); this.newItem(); }
    });
  }
}
