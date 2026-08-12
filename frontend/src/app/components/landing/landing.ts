import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { WhatsAppWidget } from '../whatsapp-widget/whatsapp-widget';

@Component({
  selector: 'app-landing',
  imports: [RouterLink, WhatsAppWidget],
  templateUrl: './landing.html',
  styleUrl: './landing.css',
})
export class Landing {}
