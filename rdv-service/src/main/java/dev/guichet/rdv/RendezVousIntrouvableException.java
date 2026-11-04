package dev.guichet.rdv;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RendezVousIntrouvableException extends RuntimeException {

    public RendezVousIntrouvableException(UUID id) {
        super("Rendez-vous introuvable : " + id);
    }
}
