import { Component } from '@angular/core';

@Component({
  selector: 'app-whatsapp-widget',
  imports: [],
  templateUrl: './whatsapp-widget.html',
  styleUrl: './whatsapp-widget.css',
})
export class WhatsAppWidget {
  phoneNumber = '34744601861';
  message = 'Hola! Me interesa hacer un pedido en Belieta.';

  getWhatsAppUrl(): string {
    return `https://wa.me/${this.phoneNumber}?text=${encodeURIComponent(this.message)}`;
  }
}
