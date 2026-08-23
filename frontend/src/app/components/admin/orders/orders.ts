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
  showDeleteConfirm = signal(false);
  deleteTargetId: number | null = null;
  searchQuery = '';
  formItem: Order = this.getEmptyOrder();

  statuses = [
    { value: 'PENDIENTE', label: 'Pendiente', icon: '&#9203;' },
    { value: 'EN_PROCESO', label: 'En Proceso', icon: '&#128296;' },
    { value: 'COMPLETADO', label: 'Completado', icon: '&#9989;' },
    { value: 'CANCELADO', label: 'Cancelado', icon: '&#10060;' }
  ];

  tamanos = ['XS', 'S', 'M', 'L', 'XL'];
  personalizaciones = ['Sin personalizacion', 'Papeleria', 'Papel de Azucar', 'Mezcla'];

  constructor(private apiService: ApiService) {}

  ngOnInit(): void {
    this.loadOrders();
  }

  getEmptyOrder(): Order {
    return {
      customerName: '',
      customerPhone: '',
      tartaName: '',
      tartaSize: 'M',
      personalization: 'Sin personalizacion',
      notes: '',
      status: 'PENDIENTE',
      totalAmount: 0
    };
  }

  loadOrders(): void {
    this.loading.set(true);
    this.apiService.getOrders(this.statusFilter() || undefined).subscribe({
      next: (data) => { this.items.set(data); this.loading.set(false); },
      error: () => { this.items.set([]); this.loading.set(false); }
    });
  }

  search(): void {
    if (!this.searchQuery.trim()) {
      this.loadOrders();
      return;
    }
    this.loading.set(true);
    this.apiService.getOrders(undefined).subscribe({
      next: (data) => {
        const q = this.searchQuery.toLowerCase();
        const filtered = data.filter(o =>
          o.customerName?.toLowerCase().includes(q) ||
          o.tartaName?.toLowerCase().includes(q) ||
          o.customerPhone?.includes(q)
        );
        this.items.set(filtered);
        this.loading.set(false);
      },
      error: () => { this.items.set([]); this.loading.set(false); }
    });
  }

  newItem(): void {
    this.formItem = this.getEmptyOrder();
    this.isNewItem.set(true);
  }

  selectItem(item: Order): void {
    this.formItem = { ...item };
    this.isNewItem.set(false);
  }

  saveItem(): void {
    if (!this.formItem.customerName?.trim()) return;
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

  quickStatus(order: Order, newStatus: string): void {
    if (!order.id) return;
    this.apiService.updateOrderStatus(order.id, newStatus).subscribe({
      next: (updated) => {
        this.items.set(this.items().map(o => o.id === updated.id ? updated : o));
      }
    });
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
    this.apiService.deleteOrder(this.deleteTargetId).subscribe({
      next: () => {
        this.loadOrders();
        this.showDeleteConfirm.set(false);
        this.deleteTargetId = null;
        if (this.formItem.id === this.deleteTargetId) this.newItem();
      }
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

  getStatusLabel(status: string): string {
    const s = this.statuses.find(st => st.value === status);
    return s ? s.label : status;
  }

  getShortDate(date: string | undefined): string {
    if (!date) return '-';
    const d = new Date(date);
    return d.toLocaleDateString('es-ES', { day: '2-digit', month: 'short', year: '2-digit' });
  }

  getStats(): { total: number; pendientes: number; enProceso: number; completados: number; totalRevenue: number } {
    const all = this.items();
    return {
      total: all.length,
      pendientes: all.filter(o => o.status === 'PENDIENTE').length,
      enProceso: all.filter(o => o.status === 'EN_PROCESO').length,
      completados: all.filter(o => o.status === 'COMPLETADO').length,
      totalRevenue: all.filter(o => o.status === 'COMPLETADO').reduce((sum, o) => sum + (o.totalAmount || 0), 0)
    };
  }
}
