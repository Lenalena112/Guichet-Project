package dev.guichet.notification;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/** Pas de vrai envoi (mail, SMS) : on garde les notifications en mémoire pour pouvoir les consulter. */
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final List<Notification> notifications = new CopyOnWriteArrayList<>();

    @GetMapping
    public List<Notification> lister() {
        return List.copyOf(notifications);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Notification creer(@Valid @RequestBody NouvelleNotification demande) {
        var notification = new Notification(UUID.randomUUID(), demande.destinataire(), demande.message(), Instant.now());
        notifications.add(notification);
        return notification;
    }
}
