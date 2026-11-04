package dev.guichet.rdv;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/** Appel de service à service : rdv-service prévient notification-service. */
@Component
public class NotificationClient {

    private final RestClient restClient;

    public NotificationClient(RestClient.Builder builder,
                              @Value("${guichet.notification-service.url}") String url) {
        this.restClient = builder.baseUrl(url).build();
    }

    public void notifier(String destinataire, String message) {
        restClient.post()
                .uri("/api/notifications")
                .body(new Notification(destinataire, message))
                .retrieve()
                .toBodilessEntity();
    }

    private record Notification(String destinataire, String message) {
    }
}
