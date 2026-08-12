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

  getStatusColor(status: string): string {
    switch (status) {
      case 'PENDIENTE': return 'bg-yellow-100 text-yellow-800';
      case 'EN_PROCESO': return 'bg-blue-100 text-blue-800';
      case 'COMPLETADO': return 'bg-green-100 text-green-800';
      case 'CANCELADO': return 'bg-red-100 text-red-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  }
}
