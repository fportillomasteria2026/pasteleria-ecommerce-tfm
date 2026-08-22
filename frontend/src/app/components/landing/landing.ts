import { Component, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { WhatsAppWidget } from '../whatsapp-widget/whatsapp-widget';
import { ChatWidget } from '../chat-widget/chat-widget';

interface Tarta {
  id: number;
  nombre: string;
  descripcion?: string;
  imagenUrl?: string;
  precioPublico: number;
}

@Component({
  selector: 'app-landing',
  imports: [RouterLink, WhatsAppWidget, ChatWidget],
  templateUrl: './landing.html',
  styleUrl: './landing.css',
})
export class Landing implements OnInit {
  menuOpen = false;
  tartasDestacadas = signal<Tarta[]>([]);
  private apiUrl = 'https://belieta-backend.onrender.com/api/tartas';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.http.get<Tarta[]>(this.apiUrl).subscribe({
      next: (data) => {
        this.tartasDestacadas.set(data.slice(0, 3));
      },
      error: () => {
        this.tartasDestacadas.set([]);
      }
    });
  }
}
