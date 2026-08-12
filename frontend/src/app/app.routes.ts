import { Routes } from '@angular/router';
import { Landing } from './components/landing/landing';
import { Gallery } from './components/gallery/gallery';
import { Login } from './components/admin/login/login';
import { AdminDashboard } from './components/admin/admin-dashboard/admin-dashboard';
import { ProductUpload } from './components/admin/product-upload/product-upload';
import { Inventory } from './components/admin/inventory/inventory';
import { Orders } from './components/admin/orders/orders';
import { adminGuard } from './guards/admin-guard';

export const routes: Routes = [
  { path: '', component: Landing },
  { path: 'gallery', component: Gallery },
  { path: 'admin/login', component: Login },
  { path: 'admin/dashboard', component: AdminDashboard, canActivate: [adminGuard] },
  { path: 'admin/products/upload', component: ProductUpload, canActivate: [adminGuard] },
  { path: 'admin/inventory', component: Inventory, canActivate: [adminGuard] },
  { path: 'admin/orders', component: Orders, canActivate: [adminGuard] },
  { path: '**', redirectTo: '' }
];
