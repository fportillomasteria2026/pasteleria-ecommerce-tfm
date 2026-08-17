import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ApiService, ProductImage } from '../../services/api';
import { WhatsAppWidget } from '../whatsapp-widget/whatsapp-widget';
import { ChatWidget } from '../chat-widget/chat-widget';

@Component({
  selector: 'app-gallery',
  imports: [FormsModule, WhatsAppWidget, ChatWidget],
  templateUrl: './gallery.html',
  styleUrl: './gallery.css',
})
export class Gallery implements OnInit {
  products = signal<ProductImage[]>([]);
  loading = signal(true);
  searchQuery = '';
  searchFocused = false;

  constructor(private apiService: ApiService) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.loading.set(true);
    this.apiService.getProducts().subscribe({
      next: (data) => {
        this.products.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.products.set([]);
        this.loading.set(false);
      }
    });
  }

  search(): void {
    if (!this.searchQuery.trim()) {
      this.loadProducts();
      return;
    }

    const tagMatch = this.searchQuery.match(/#\w+/g);
    if (tagMatch && tagMatch.length > 0) {
      this.loading.set(true);
      this.apiService.searchByHashtags(tagMatch).subscribe({
        next: (data) => { this.products.set(data); this.loading.set(false); },
        error: () => { this.loading.set(false); }
      });
    } else {
      this.loading.set(true);
      this.apiService.searchProducts(this.searchQuery).subscribe({
        next: (data) => { this.products.set(data); this.loading.set(false); },
        error: () => { this.loading.set(false); }
      });
    }
  }

  filterByTag(tag: string): void {
    this.searchQuery = tag;
    this.search();
  }
}
