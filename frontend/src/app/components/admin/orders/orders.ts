import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DatePipe, CurrencyPipe } from '@angular/common';
import { ApiService, Order } from '../../../services/api';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-orders',
  imports: [FormsModule, DatePipe, CurrencyPipe, RouterLink],
  templateUrl: './orders.html',
  styleUrl: './orders.css',
})
export class Orders implements OnInit {
  items = signal<Order[]>([]);
  statusFilter = signal('');
  loading = signal(true);
  isNewItem = signal(false);
  formItem: Order = { customerName: '', status: 'PENDIENTE', totalAmount: 0 };

  constructor(private apiService: ApiService) {}

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders(): void {
    this.loading.set(true);
    this.apiService.getOrders(this.statusFilter() || undefined).subscribe({
      next: (data) => { this.items.set(data); this.loading.set(false); },
      error: () => { this.items.set([]); this.loading.set(false); }
    });
  }

  newItem(): void {
    this.formItem = { customerName: '', status: 'PENDIENTE', totalAmount: 0 };
    this.isNewItem.set(true);
  }

  selectItem(item: Order): void {
    this.formItem = { ...item };
    this.isNewItem.set(false);
  }

  saveItem(): void {
    if (!this.formItem.customerName) return;
    if (this.isNewItem()) {
      this.apiService.createOrder(this.formItem).subscribe({
        next: () => { this.loadOrders(); this.newItem(); }
      });
    } else {
      if (!this.formItem.id) return;
      this.apiService.updateOrder(this.formItem.id, this.formItem).subscribe({
        next: () => { this.loadOrders(); this.newItem(); }
      });
    }
  }

  deleteItem(id: number): void {
    this.apiService.deleteOrder(id).subscribe({
      next: () => { this.loadOrders(); this.newItem(); }
    });
  }

  getStatusBg(status: string): string {
    switch (status) {
      case 'PENDIENTE': return '#FEF3C7';
      case 'EN_PROCESO': return '#DBEAFE';
      case 'COMPLETADO': return '#DCFCE7';
      case 'CANCELADO': return '#FEE2E2';
      default: return '#F3F4F6';
    }
  }

  getStatusColor(status: string): string {
    switch (status) {
      case 'PENDIENTE': return '#92400E';
      case 'EN_PROCESO': return '#1E40AF';
      case 'COMPLETADO': return '#166534';
      case 'CANCELADO': return '#991B1B';
      default: return '#374151';
    }
  }
}
