import { Component, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { RouterLink } from '@angular/router';

interface Sugerencia {
  nombre: string;
  proveedor: string;
  stockActual: number;
  unidad: string;
  cantidadSugerida: number;
  costeEstimado: number;
  prioridad: string;
  motivo: string;
}

interface MateriaPrima {
  id: number;
  nombre: string;
  marca: string;
  proveedor: string;
  coste: number;
  formato: string;
  peso: number;
  unidad: string;
  cantidad: number;
}

@Component({
  selector: 'app-inventory-ai',
  imports: [RouterLink],
  templateUrl: './inventory-ai.html',
  styleUrl: './inventory-ai.css',
})
export class InventoryAi {
  loading = signal(false);
  analyzing = signal(false);
  materiaPrima = signal<MateriaPrima[]>([]);
  sugerencias = signal<Sugerencia[]>([]);
  error = signal('');
  private apiUrl = 'https://belieta-backend.onrender.com/api/admin/materia-prima';

  constructor(private http: HttpClient) {}

  loadInventory(): void {
    this.loading.set(true);
    this.http.get<MateriaPrima[]>(this.apiUrl).subscribe({
      next: (data) => { this.materiaPrima.set(data); this.loading.set(false); },
      error: () => { this.error.set('Error al cargar inventario'); this.loading.set(false); }
    });
  }

  analyze(): void {
    if (this.materiaPrima().length === 0) {
      this.loadInventory();
      return;
    }
    this.analyzing.set(true);
    this.error.set('');
    this.http.post<{ sugerencias: Sugerencia[] }>(
      'https://belieta-backend.onrender.com/api/admin/ai/inventory-optimize',
      this.materiaPrima()
    ).subscribe({
      next: (res) => {
        this.sugerencias.set(res.sugerencias || []);
        this.analyzing.set(false);
      },
      error: () => {
        this.error.set('Error al analizar inventario con IA');
        this.analyzing.set(false);
      }
    });
  }

  getPrioridadColor(prioridad: string): string {
    switch (prioridad) {
      case 'alta': return '#DC2626';
      case 'media': return '#F59E0B';
      case 'baja': return '#22C55E';
      default: return '#8B7355';
    }
  }

  getPrioridadBg(prioridad: string): string {
    switch (prioridad) {
      case 'alta': return '#FEE2E2';
      case 'media': return '#FEF3C7';
      case 'baja': return '#D1FAE5';
      default: return '#F0E6D6';
    }
  }

  getTotalCoste(): number {
    return this.sugerencias().reduce((sum, s) => sum + (s.costeEstimado || 0), 0);
  }
}
