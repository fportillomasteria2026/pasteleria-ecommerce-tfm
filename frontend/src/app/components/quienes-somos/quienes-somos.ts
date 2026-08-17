import { Component } from '@angular/core';
import { WhatsAppWidget } from '../whatsapp-widget/whatsapp-widget';
import { ChatWidget } from '../chat-widget/chat-widget';

@Component({
  selector: 'app-quienes-somos',
  imports: [WhatsAppWidget, ChatWidget],
  templateUrl: './quienes-somos.html',
  styleUrl: './quienes-somos.css',
})
export class QuienesSomos {}
