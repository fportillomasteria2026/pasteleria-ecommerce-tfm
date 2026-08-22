import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { WhatsAppWidget } from '../whatsapp-widget/whatsapp-widget';
import { ChatWidget } from '../chat-widget/chat-widget';

@Component({
  selector: 'app-landing',
  imports: [RouterLink, WhatsAppWidget, ChatWidget],
  templateUrl: './landing.html',
  styleUrl: './landing.css',
})
export class Landing {
  menuOpen = false;
}
