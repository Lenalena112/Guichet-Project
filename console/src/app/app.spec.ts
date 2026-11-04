import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { App } from './app';

describe('App', () => {
  let http: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [App],
      providers: [provideHttpClient(), provideHttpClientTesting()]
    }).compileComponents();
    http = TestBed.inject(HttpTestingController);
  });

  it('affiche les rendez-vous et les notifications', async () => {
    const fixture = TestBed.createComponent(App);
    fixture.detectChanges();

    http.expectOne('/api/rendez-vous').flush([
      { id: '1', demandeur: 'ines.martin', conseiller: 'maelle.le-goff', debut: '2099-01-15T10:00:00', objet: 'Bilan' }
    ]);
    http.expectOne('/api/notifications').flush([
      { id: '2', destinataire: 'ines.martin', message: 'Rendez-vous demain', creeeLe: '2099-01-14T10:00:00Z' }
    ]);
    await fixture.whenStable();

    const page = fixture.nativeElement as HTMLElement;
    expect(page.querySelector('tbody')?.textContent).toContain('Bilan');
    expect(page.querySelector('aside')?.textContent).toContain('Rendez-vous demain');
  });

  afterEach(() => http.verify());
});
