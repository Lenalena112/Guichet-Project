package dev.guichet.notification;

import java.time.Instant;
import java.util.UUID;

public record Notification(UUID id, String destinataire, String message, Instant creeeLe) {
}
