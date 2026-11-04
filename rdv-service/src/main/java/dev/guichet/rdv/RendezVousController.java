package dev.guichet.rdv;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rendez-vous")
public class RendezVousController {

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy 'à' HH'h'mm");

    private final RendezVousRepository repository;
    private final NotificationClient notificationClient;

    public RendezVousController(RendezVousRepository repository, NotificationClient notificationClient) {
        this.repository = repository;
        this.notificationClient = notificationClient;
    }

    @GetMapping
    public List<RendezVous> lister() {
        return repository.tous();
    }

    @GetMapping("/{id}")
    public RendezVous consulter(@PathVariable UUID id) {
        return repository.parId(id).orElseThrow(() -> new RendezVousIntrouvableException(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RendezVous creer(@Valid @RequestBody NouveauRendezVous demande) {
        var rdv = repository.enregistrer(new RendezVous(UUID.randomUUID(), demande.demandeur(),
                demande.conseiller(), demande.debut(), demande.objet()));
        notificationClient.notifier(rdv.demandeur(),
                "Rendez-vous « " + rdv.objet() + " » le " + rdv.debut().format(FORMAT));
        return rdv;
    }
}
