import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

export interface RendezVous {
  id: string;
  demandeur: string;
  conseiller: string;
  debut: string;
  objet: string;
}

export type NouveauRendezVous = Omit<RendezVous, 'id'>;

export interface Notification {
  id: string;
  destinataire: string;
  message: string;
  creeeLe: string;
}

/** Appels aux deux services. Les URL sont relatives : en développement, proxy.conf.json les redirige. */
@Injectable({ providedIn: 'root' })
export class GuichetApi {
  private readonly http = inject(HttpClient);

  listerRendezVous(): Observable<RendezVous[]> {
    return this.http.get<RendezVous[]>('/api/rendez-vous');
  }

  creerRendezVous(rdv: NouveauRendezVous): Observable<RendezVous> {
    return this.http.post<RendezVous>('/api/rendez-vous', rdv);
  }

  listerNotifications(): Observable<Notification[]> {
    return this.http.get<Notification[]>('/api/notifications');
  }
}
