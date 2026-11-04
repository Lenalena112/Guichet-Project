package dev.guichet.rdv;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Stockage en mémoire : pas de base de données, ce n'est pas le sujet de ce projet. */
@Repository
public class RendezVousRepository {

    private final Map<UUID, RendezVous> rendezVous = new ConcurrentHashMap<>();

    public RendezVousRepository() {
        var demain = LocalDate.now().plusDays(1);
        enregistrer(new RendezVous(UUID.randomUUID(), "ines.martin", "maelle.le-goff",
                demain.atTime(LocalTime.of(9, 30)), "Point mensuel"));
        enregistrer(new RendezVous(UUID.randomUUID(), "nolwenn.kerjean", "maelle.le-goff",
                demain.atTime(LocalTime.of(11, 0)), "Projet de formation"));
        enregistrer(new RendezVous(UUID.randomUUID(), "theo.bernard", "yanis.haddad",
                demain.plusDays(1).atTime(LocalTime.of(14, 0)), "Atelier CV"));
    }

    public List<RendezVous> tous() {
        return rendezVous.values().stream()
                .sorted(Comparator.comparing(RendezVous::debut))
                .toList();
    }

    public Optional<RendezVous> parId(UUID id) {
        return Optional.ofNullable(rendezVous.get(id));
    }

    public RendezVous enregistrer(RendezVous rdv) {
        rendezVous.put(rdv.id(), rdv);
        return rdv;
    }
}
