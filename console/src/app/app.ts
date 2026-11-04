import { DatePipe } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { GuichetApi, Notification, RendezVous } from './guichet-api';

@Component({
  selector: 'app-root',
  imports: [DatePipe, ReactiveFormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
  private readonly api = inject(GuichetApi);

  protected readonly rendezVous = signal<RendezVous[]>([]);
  protected readonly notifications = signal<Notification[]>([]);
  protected readonly erreur = signal<string | null>(null);

  protected readonly formulaire = inject(NonNullableFormBuilder).group({
    demandeur: ['', Validators.required],
    conseiller: ['', Validators.required],
    debut: ['', Validators.required],
    objet: ['', Validators.required]
  });

  ngOnInit(): void {
    this.rafraichir();
  }

  protected creer(): void {
    if (this.formulaire.invalid) {
      return;
    }
    this.api.creerRendezVous(this.formulaire.getRawValue()).subscribe({
      next: () => {
        this.formulaire.reset();
        this.rafraichir();
      },
      error: () => this.erreur.set('Création impossible : vérifiez que la date est dans le futur.')
    });
  }

  private rafraichir(): void {
    this.erreur.set(null);
    this.api.listerRendezVous().subscribe({
      next: (rdv) => this.rendezVous.set(rdv),
      error: () => this.erreur.set('rdv-service ne répond pas (port 8081).')
    });
    this.api.listerNotifications().subscribe({
      next: (notifications) => this.notifications.set(notifications),
      error: () => this.erreur.set('notification-service ne répond pas (port 8082).')
    });
  }
}
