import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ProductImage {
  id: number;
  imageUrl: string;
  title: string;
  description: string;
  createdAt: string;
  hashtags: string[];
}

export interface AiHashtagsResponse {
  imageUrl: string;
  hashtags: string[];
}

export interface Ingredient {
  id?: number;
  name: string;
  stockQuantity: number;
  unit: string;
  category?: string;
}

export interface Recipe {
  id?: number;
  name: string;
  instructions: string;
}

export interface MateriaPrima {
  id?: number;
  codigoSku?: string;
  nombre: string;
  marca?: string;
  proveedor?: string;
  coste?: number;
  formato?: string;
  peso?: number;
  unidad?: string;
  cantidad?: number;
}

export interface Order {
  id?: number;
  customerName: string;
  status: string;
  totalAmount: number;
  createdAt?: string;
}

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = 'https://dulce sabor-backend.onrender.com/api';

  constructor(private http: HttpClient) {}

  getProducts(): Observable<ProductImage[]> {
    return this.http.get<ProductImage[]>(`${this.baseUrl}/products`);
  }

  searchProducts(query: string): Observable<ProductImage[]> {
    return this.http.get<ProductImage[]>(`${this.baseUrl}/products/search?query=${query}`);
  }

  searchByHashtags(tags: string[]): Observable<ProductImage[]> {
    const params = tags.map(t => `tags=${encodeURIComponent(t)}`).join('&');
    return this.http.get<ProductImage[]>(`${this.baseUrl}/products/by-hashtags?${params}`);
  }

  uploadProduct(formData: FormData): Observable<AiHashtagsResponse> {
    return this.http.post<AiHashtagsResponse>(`${this.baseUrl}/admin/products/upload`, formData);
  }

  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/admin/products/${id}`);
  }

  getIngredients(search?: string): Observable<Ingredient[]> {
    const params = search ? `?search=${search}` : '';
    return this.http.get<Ingredient[]>(`${this.baseUrl}/admin/ingredients${params}`);
  }

  createIngredient(ingredient: Ingredient): Observable<Ingredient> {
    return this.http.post<Ingredient>(`${this.baseUrl}/admin/ingredients`, ingredient);
  }

  updateIngredient(id: number, ingredient: Ingredient): Observable<Ingredient> {
    return this.http.put<Ingredient>(`${this.baseUrl}/admin/ingredients/${id}`, ingredient);
  }

  deleteIngredient(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/admin/ingredients/${id}`);
  }

  getRecipes(search?: string): Observable<Recipe[]> {
    const params = search ? `?search=${search}` : '';
    return this.http.get<Recipe[]>(`${this.baseUrl}/admin/recipes${params}`);
  }

  createRecipe(recipe: Recipe): Observable<Recipe> {
    return this.http.post<Recipe>(`${this.baseUrl}/admin/recipes`, recipe);
  }

  updateRecipe(id: number, recipe: Recipe): Observable<Recipe> {
    return this.http.put<Recipe>(`${this.baseUrl}/admin/recipes/${id}`, recipe);
  }

  deleteRecipe(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/admin/recipes/${id}`);
  }

  getOrders(status?: string): Observable<Order[]> {
    const params = status ? `?status=${status}` : '';
    return this.http.get<Order[]>(`${this.baseUrl}/admin/orders${params}`);
  }

  createOrder(order: Order): Observable<Order> {
    return this.http.post<Order>(`${this.baseUrl}/admin/orders`, order);
  }

  updateOrder(id: number, order: Order): Observable<Order> {
    return this.http.put<Order>(`${this.baseUrl}/admin/orders/${id}`, order);
  }

  deleteOrder(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/admin/orders/${id}`);
  }

  // Materia Prima
  getMateriaPrima(): Observable<MateriaPrima[]> {
    return this.http.get<MateriaPrima[]>(`${this.baseUrl}/admin/materia-prima`);
  }

  getMateriaPrimaByCategoria(categoria: string): Observable<MateriaPrima[]> {
    return this.http.get<MateriaPrima[]>(`${this.baseUrl}/admin/materia-prima/categoria/${categoria}`);
  }

  createMateriaPrima(item: MateriaPrima): Observable<MateriaPrima> {
    return this.http.post<MateriaPrima>(`${this.baseUrl}/admin/materia-prima`, item);
  }

  updateMateriaPrima(id: number, item: MateriaPrima): Observable<MateriaPrima> {
    return this.http.put<MateriaPrima>(`${this.baseUrl}/admin/materia-prima/${id}`, item);
  }

  deleteMateriaPrima(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/admin/materia-prima/${id}`);
  }
}
