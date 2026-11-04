package dev.guichet.rdv;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record NouveauRendezVous(
        @NotBlank String demandeur,
        @NotBlank String conseiller,
        @NotNull @Future LocalDateTime debut,
        @NotBlank String objet) {
}
