import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

interface ChatMessage {
  text: string;
  isUser: boolean;
  time: string;
}

@Component({
  selector: 'app-chat-widget',
  imports: [FormsModule],
  templateUrl: './chat-widget.html',
  styleUrl: './chat-widget.css',
})
export class ChatWidget {
  isOpen = signal(false);
  userInput = '';
  messages = signal<ChatMessage[]>([
    { text: 'Hola! Soy el asistente de Dulce Sabor. En que puedo ayudarte?', isUser: false, time: this.getTime() }
  ]);
  isLoading = signal(false);
  private backendUrl = 'https://belieta-backend.onrender.com';

  constructor(private http: HttpClient) {}

  toggle(): void {
    this.isOpen.set(!this.isOpen());
  }

  send(): void {
    const msg = this.userInput.trim();
    if (!msg || this.isLoading()) return;

    this.messages.update(m => [...m, { text: msg, isUser: true, time: this.getTime() }]);
    this.userInput = '';
    this.isLoading.set(true);

    const handleError = (error: any) => {
      let msg = 'Disculpa, ha habido un error. Inténtalo de nuevo.';
      try {
        if (error?.error?.reply) msg = error.error.reply;
        else if (error?.error?.message) msg = error.error.message;
        else if (error?.message) msg = `Error: ${error.message}`;
      } catch {}
      console.error('[ChatWidget] Error:', error);
      this.messages.update(m => [...m, { text: msg, isUser: false, time: this.getTime() }]);
      this.isLoading.set(false);
    };

    this.http.post<{ reply: string }>(`${this.backendUrl}/api/chat`, { message: msg }).subscribe({
      next: (res) => {
        this.messages.update(m => [...m, { text: res.reply, isUser: false, time: this.getTime() }]);
        this.isLoading.set(false);
      },
      error: handleError
    });
  }

  private getTime(): string {
    return new Date().toLocaleTimeString('es-ES', { hour: '2-digit', minute: '2-digit' });
  }
}
