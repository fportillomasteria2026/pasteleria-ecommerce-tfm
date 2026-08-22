import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { RouterLink } from '@angular/router';

interface Tarta {
  id?: number;
  sku?: string;
  nombre: string;
  descripcion?: string;
  imagenUrl?: string;
  hashtags: string;
  tamano: string;
  pisos: number;
  forma: string;
  dimensiones?: string;
  saborBizcocho?: string;
  frutas?: string;
  tipoCrema?: string;
  tipoPersonalizacion?: string;
  precioPublico: number;
  coste?: number;
  disponible: boolean;
  activo?: boolean;
  notas?: string;
}

@Component({
  selector: 'app-tartas',
  imports: [FormsModule, RouterLink],
  templateUrl: './tartas.html',
  styleUrl: './tartas.css',
})
export class Tartas implements OnInit {
  items = signal<Tarta[]>([]);
  loading = signal(true);
  isNewItem = signal(false);
  formItem: Tarta = this.getEmptyItem();
  searchQuery = '';
  hashtagsInput = '';
  showImageDialog = signal(false);
  serverImages = signal<string[]>([]);

  tamanos = ['XS', 'S', 'M', 'L', 'XL'];
  formas = ['Cilindrica', 'Cuadrada', 'Rectangular'];
  sabores = ['Chocolate', 'Vainilla', 'Red Velvet', 'Fresa', 'Zanahoria', 'Limón', 'Nuez', 'Coco', 'Otro'];
  cremas = ['Buttercream', 'Ganache', 'Crema Chantilly', 'Mousse', 'Glaceau', 'Relleno de Fruta', 'Otro'];
  personalizaciones = ['Papeleria', 'Papel de Azucar', 'Mezcla', 'Sin Personalizacion'];

  private apiUrl = 'https://belieta-backend.onrender.com/api/admin/tartas';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadItems();
  }

  loadItems(): void {
    this.loading.set(true);
    this.http.get<Tarta[]>(this.apiUrl).subscribe({
      next: (data) => { this.items.set(data); this.loading.set(false); },
      error: () => { this.items.set([]); this.loading.set(false); }
    });
  }

  getEmptyItem(): Tarta {
    return {
      nombre: '',
      descripcion: '',
      hashtags: '',
      tamano: 'M',
      pisos: 2,
      forma: 'Cilindrica',
      dimensiones: '',
      saborBizcocho: '',
      frutas: '',
      tipoCrema: '',
      tipoPersonalizacion: 'Sin Personalizacion',
      precioPublico: 0,
      coste: 0,
      disponible: true,
      notas: ''
    };
  }

  newItem(): void {
    this.formItem = this.getEmptyItem();
    this.isNewItem.set(true);
  }

  selectItem(item: Tarta): void {
    this.formItem = { ...item };
    this.isNewItem.set(false);
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      this.formItem.imagenUrl = 'images/tartas/' + file.name;
    }
  }

  openImageDialog(): void {
    this.loadServerImages();
    this.showImageDialog.set(true);
  }

  closeImageDialog(): void {
    this.showImageDialog.set(false);
  }

  loadServerImages(): void {
    const images = [
      'images/tartas/tarta_chocolate.jpg',
      'images/tartas/tarta_vainilla.jpg',
      'images/tartas/red_velvet.jpg'
    ];
    this.serverImages.set(images);
  }

  selectServerImage(img: string): void {
    this.formItem.imagenUrl = img;
    this.showImageDialog.set(false);
  }

  saveItem(): void {
    if (!this.formItem.nombre) return;
    if (this.isNewItem()) {
      this.http.post<Tarta>(this.apiUrl, this.formItem).subscribe({
        next: () => { this.loadItems(); this.newItem(); }
      });
    } else {
      if (!this.formItem.id) return;
      this.http.put<Tarta>(`${this.apiUrl}/${this.formItem.id}`, this.formItem).subscribe({
        next: () => { this.loadItems(); this.newItem(); }
      });
    }
  }

  deleteItem(id: number): void {
    this.http.delete(`${this.apiUrl}/${id}`).subscribe({
      next: () => { this.loadItems(); this.newItem(); }
    });
  }

  search(): void {
    if (!this.searchQuery.trim()) {
      this.loadItems();
      return;
    }
    this.http.get<Tarta[]>(`${this.apiUrl}/search?q=${this.searchQuery}`).subscribe({
      next: (data) => { this.items.set(data); this.loading.set(false); },
      error: () => { this.loading.set(false); }
    });
  }
}
