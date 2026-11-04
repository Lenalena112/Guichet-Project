package dev.guichet.notification.securite;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class RolesKeycloakConverterTest {

    private final RolesKeycloakConverter converter = new RolesKeycloakConverter();

    @Test
    void traduitLesRolesDuRealmEnAutoritesSpring() {
        var jwt = jwtAvecClaims(Map.of("realm_access", Map.of("roles", List.of("conseiller", "offline_access"))));

        assertThat(converter.convert(jwt))
                .extracting(GrantedAuthority::getAuthority)
                .containsExactlyInAnyOrder("ROLE_conseiller", "ROLE_offline_access");
    }

    @Test
    void aucuneAutoriteSansClaimRealmAccess() {
        var jwt = jwtAvecClaims(Map.of("preferred_username", "ines.martin"));

        assertThat(converter.convert(jwt)).isEmpty();
    }

    @Test
    void aucuneAutoriteSiLesRolesNeSontPasUneListe() {
        var jwt = jwtAvecClaims(Map.of("realm_access", Map.of("roles", "conseiller")));

        assertThat(converter.convert(jwt)).isEmpty();
    }

    private static Jwt jwtAvecClaims(Map<String, Object> claims) {
        return Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .claims(c -> c.putAll(claims))
                .build();
    }
}
