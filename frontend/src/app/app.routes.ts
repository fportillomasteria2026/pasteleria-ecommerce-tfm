import { Routes } from '@angular/router';
import { Landing } from './components/landing/landing';
import { Gallery } from './components/gallery/gallery';
import { QuienesSomos } from './components/quienes-somos/quienes-somos';
import { Login } from './components/admin/login/login';
import { AdminDashboard } from './components/admin/admin-dashboard/admin-dashboard';
import { ProductUpload } from './components/admin/product-upload/product-upload';
import { Ingredients } from './components/admin/ingredients/ingredients';
import { Recipes } from './components/admin/recipes/recipes';
import { Orders } from './components/admin/orders/orders';
import { Tartas } from './components/admin/tartas/tartas';
import { adminGuard } from './guards/admin-guard';

export const routes: Routes = [
  { path: '', component: Landing },
  { path: 'gallery', component: Gallery },
  { path: 'quienes-somos', component: QuienesSomos },
  { path: 'admin/login', component: Login },
  { path: 'admin/dashboard', component: AdminDashboard, canActivate: [adminGuard] },
  { path: 'admin/products/upload', component: ProductUpload, canActivate: [adminGuard] },
  { path: 'admin/tartas', component: Tartas, canActivate: [adminGuard] },
  { path: 'admin/ingredients', component: Ingredients, canActivate: [adminGuard] },
  { path: 'admin/recipes', component: Recipes, canActivate: [adminGuard] },
  { path: 'admin/orders', component: Orders, canActivate: [adminGuard] },
  { path: '**', redirectTo: '' }
];
