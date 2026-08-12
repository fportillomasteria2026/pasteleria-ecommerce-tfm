import { Component } from '@angular/core';

@Component({
  selector: 'app-whatsapp-widget',
  imports: [],
  templateUrl: './whatsapp-widget.html',
  styleUrl: './whatsapp-widget.css',
})
export class WhatsAppWidget {
  phoneNumber = '34600000000';
  message = 'Hola! Me interesa hacer un pedido en Pasteleria Dulce Arte.';

  getWhatsAppUrl(): string {
    return `https://wa.me/${this.phoneNumber}?text=${encodeURIComponent(this.message)}`;
  }
}
