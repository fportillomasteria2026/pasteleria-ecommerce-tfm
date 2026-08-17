import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DatePipe, CurrencyPipe } from '@angular/common';
import { ApiService, Order } from '../../../services/api';

@Component({
  selector: 'app-orders',
  imports: [FormsModule, DatePipe, CurrencyPipe],
  templateUrl: './orders.html',
  styleUrl: './orders.css',
})
export class Orders implements OnInit {
  orders = signal<Order[]>([]);
  statusFilter = signal('');
  loading = signal(true);

  newOrder: Order = { customerName: '', status: 'PENDIENTE', totalAmount: 0 };
  editingOrder = signal<Order | null>(null);

  constructor(private apiService: ApiService) {}

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders(): void {
    this.loading.set(true);
    this.apiService.getOrders(this.statusFilter() || undefined).subscribe({
      next: (data) => { this.orders.set(data); this.loading.set(false); },
      error: () => { this.orders.set([]); this.loading.set(false); }
    });
  }

  addOrder(): void {
    if (!this.newOrder.customerName) return;
    this.apiService.createOrder(this.newOrder).subscribe({
      next: (order) => {
        this.orders.update(list => [...list, order]);
        this.newOrder = { customerName: '', status: 'PENDIENTE', totalAmount: 0 };
      }
    });
  }

  updateOrder(order: Order): void {
    if (!order.id) return;
    this.apiService.updateOrder(order.id, order).subscribe({
      next: () => {
        this.editingOrder.set(null);
        this.loadOrders();
      }
    });
  }

  deleteOrder(id: number): void {
    this.apiService.deleteOrder(id).subscribe({
      next: () => this.loadOrders()
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

  getStatusTextColor(status: string): string {
    switch (status) {
      case 'PENDIENTE': return '#92400E';
      case 'EN_PROCESO': return '#1E40AF';
      case 'COMPLETADO': return '#166534';
      case 'CANCELADO': return '#991B1B';
      default: return '#374151';
    }
  }
}
