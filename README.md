# Guichet

Petite application de prise de rendez-vous pour **Horizon Emploi**, un opérateur public de l'emploi **fictif**.
Toutes les données sont inventées. Le projet sert de terrain d'apprentissage pour **Keycloak**.

## Architecture

```
console (Angular, 4200) ──/api/rendez-vous──► rdv-service (8081) ──POST /api/notifications──► notification-service (8082)
                        └─/api/notifications────────────────────────────────────────────────►
```

| Module | Rôle |
|---|---|
| `rdv-service` | Liste, consulte et crée des rendez-vous (stockage en mémoire). Prévient `notification-service` à chaque création. |
| `notification-service` | Garde en mémoire les notifications reçues et les liste. |
| `console` | Interface Angular. En développement, `proxy.conf.json` redirige `/api/...` vers les services. |

Stack : Java 21, Spring Boot 4.0.9, Angular 21, Node 24.

## Lancer en local

```bash
./mvnw verify
```

Puis, dans trois terminaux (ou depuis IntelliJ) :

```bash
./mvnw -pl notification-service spring-boot:run
./mvnw -pl rdv-service spring-boot:run
cd console && npm start
```

Ouvrir http://localhost:4200.

## Feuille de route Keycloak

1. Keycloak dans Docker Compose : realm `horizon`, rôles `demandeur` et `conseiller`, utilisateurs fictifs.
2. Les deux services deviennent des *resource servers* : sans token valide, réponse 401.
3. Connexion depuis la console Angular (Authorization Code + PKCE), le token est envoyé aux API.
4. Autorisations par rôle : un demandeur ne voit que ses rendez-vous, un conseiller ceux de son portefeuille.
5. Appel de service à service : `rdv-service` obtient son propre token (*client credentials*) pour appeler `notification-service`.
