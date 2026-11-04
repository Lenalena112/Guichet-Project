package dev.guichet.rdv;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.io.IOException;

/** Appel de service à service : rdv-service prévient notification-service. */
@Component
public class NotificationClient {

    private final RestClient restClient;

    public NotificationClient(RestClient.Builder builder,
                              @Value("${guichet.notification-service.url}") String url) {
        this.restClient = builder.baseUrl(url)
                .requestInterceptor(NotificationClient::relayerTokenUtilisateur).build();
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

    /**
     * Provisoire : on retransmet le token de l'utilisateur connecté.
     * L'étape 5 le remplacera par un token propre à rdv-service (client credentials).
     */
    private static ClientHttpResponse relayerTokenUtilisateur(HttpRequest request, byte[] body,
                                                              ClientHttpRequestExecution execution) throws IOException {
        if (SecurityContextHolder.getContext().getAuthentication() instanceof JwtAuthenticationToken authentification) {
            request.getHeaders().setBearerAuth(authentification.getToken().getTokenValue());
        }
        return execution.execute(request, body);
    }
}
