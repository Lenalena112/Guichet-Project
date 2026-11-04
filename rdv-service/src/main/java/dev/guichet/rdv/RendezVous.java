package dev.guichet.rdv;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Un rendez-vous entre un demandeur et son conseiller.
 * Les deux personnes sont désignées par leur identifiant de connexion : c'est ce que Keycloak
 * mettra dans le token (claim preferred_username).
 */
public record RendezVous(UUID id, String demandeur, String conseiller, LocalDateTime debut, String objet) {
}
