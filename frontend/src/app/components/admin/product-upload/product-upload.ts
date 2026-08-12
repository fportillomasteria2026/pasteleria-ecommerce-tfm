import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../../services/api';

@Component({
  selector: 'app-product-upload',
  imports: [FormsModule],
  templateUrl: './product-upload.html',
  styleUrl: './product-upload.css',
})
export class ProductUpload {
  title = '';
  description = '';
  selectedFile: File | null = null;
  previewUrl = signal<string | null>(null);
  uploading = signal(false);
  result = signal<{ imageUrl: string; hashtags: string[] } | null>(null);
  errorMessage = signal('');

  constructor(private apiService: ApiService) {}

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
      const reader = new FileReader();
      reader.onload = () => this.previewUrl.set(reader.result as string);
      reader.readAsDataURL(this.selectedFile);
    }
  }

  upload(): void {
    if (!this.selectedFile) {
      this.errorMessage.set('Selecciona una imagen primero');
      return;
    }

    this.uploading.set(true);
    this.errorMessage.set('');
    this.result.set(null);

    const formData = new FormData();
    formData.append('image', this.selectedFile);
    if (this.title) formData.append('title', this.title);
    if (this.description) formData.append('description', this.description);

    this.apiService.uploadProduct(formData).subscribe({
      next: (response) => {
        this.result.set(response);
        this.uploading.set(false);
      },
      error: () => {
        this.errorMessage.set('Error al subir la imagen. Intentalo de nuevo.');
        this.uploading.set(false);
      }
    });
  }

  reset(): void {
    this.title = '';
    this.description = '';
    this.selectedFile = null;
    this.previewUrl.set(null);
    this.result.set(null);
    this.errorMessage.set('');
  }
}
