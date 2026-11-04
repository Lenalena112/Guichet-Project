package dev.guichet.notification;

import jakarta.validation.constraints.NotBlank;

public record NouvelleNotification(@NotBlank String destinataire, @NotBlank String message) {
}
