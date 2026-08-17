import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  username: string;
  role: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'https://belieta-backend.onrender.com/api/auth';

  private _isLoggedIn = signal(false);
  private _username = signal('');
  private _role = signal('');
  private _token = signal('');

  readonly isLoggedIn = this._isLoggedIn.asReadonly();
  readonly username = this._username.asReadonly();
  readonly role = this._role.asReadonly();

  constructor(private http: HttpClient) {
    const token = localStorage.getItem('token');
    if (token) {
      this._token.set(token);
      this._isLoggedIn.set(true);
      this._username.set(localStorage.getItem('username') || '');
      this._role.set(localStorage.getItem('role') || '');
    }
  }

  login(request: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, request).pipe(
      tap(response => {
        this._token.set(response.token);
        this._username.set(response.username);
        this._role.set(response.role);
        this._isLoggedIn.set(true);
        localStorage.setItem('token', response.token);
        localStorage.setItem('username', response.username);
        localStorage.setItem('role', response.role);
      })
    );
  }

  logout(): void {
    this._token.set('');
    this._username.set('');
    this._role.set('');
    this._isLoggedIn.set(false);
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    localStorage.removeItem('role');
  }

  getToken(): string {
    return this._token();
  }

  isAdmin(): boolean {
    return this._role() === 'ADMIN';
  }
}
