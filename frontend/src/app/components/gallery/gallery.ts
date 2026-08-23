import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

interface Tarta {
  id: number;
  sku?: string;
  nombre: string;
  descripcion?: string;
  imagenUrl?: string;
  hashtags: string[];
  tamano: string;
  pisos: number;
  forma: string;
  dimensiones?: string;
  saborBizcocho?: string;
  frutas?: string;
  tipoCrema?: string;
  tipoPersonalizacion?: string;
  precioPublico: number;
  disponible: boolean;
}

@Component({
  selector: 'app-gallery',
  imports: [FormsModule],
  templateUrl: './gallery.html',
  styleUrl: './gallery.css',
})
export class Gallery implements OnInit {
  items = signal<Tarta[]>([]);
  loading = signal(true);
  searchQuery = '';
  private apiUrl = 'https://belieta-backend.onrender.com/api/tartas';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadItems();
  }

  loadItems(): void {
    this.loading.set(true);
    this.http.get<Tarta[]>(this.apiUrl).subscribe({
      next: (data) => { this.items.set(data.map(d => ({...d, hashtags: this.parseHashtags(d.hashtags)}))); this.loading.set(false); },
      error: () => { this.items.set([]); this.loading.set(false); }
    });
  }

  search(): void {
    if (!this.searchQuery.trim()) {
      this.loadItems();
      return;
    }
    const tagMatch = this.searchQuery.match(/#\w+/g);
    if (tagMatch && tagMatch.length > 0) {
      const params = tagMatch.map(t => 'tags=' + encodeURIComponent(t)).join('&');
      this.http.get<Tarta[]>(`${this.apiUrl}/by-hashtags?${params}`).subscribe({
        next: (data) => { this.items.set(data.map(d => ({...d, hashtags: this.parseHashtags(d.hashtags)}))); this.loading.set(false); },
        error: () => { this.loading.set(false); }
      });
    } else {
      this.http.get<Tarta[]>(`${this.apiUrl}/search?q=${this.searchQuery}`).subscribe({
        next: (data) => { this.items.set(data.map(d => ({...d, hashtags: this.parseHashtags(d.hashtags)}))); this.loading.set(false); },
        error: () => { this.loading.set(false); }
      });
    }
  }

  filterByTag(tag: string): void {
    this.searchQuery = '#' + tag;
    this.search();
  }

  private parseHashtags(raw: any): string[] {
    if (Array.isArray(raw)) return raw.filter(t => t && t.trim());
    if (typeof raw === 'string' && raw.trim()) return raw.split(',').map(t => t.trim()).filter(t => t);
    return [];
  }
}
