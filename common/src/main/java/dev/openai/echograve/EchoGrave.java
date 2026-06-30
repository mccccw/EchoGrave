package dev.openai.echograve;

import java.net.URI;
import java.net.http.*;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;

/** Common, loader-neutral EchoGrave logic used by the Fabric and NeoForge entrypoints. */
public final class EchoGrave {
    public static final String MOD_ID = "echograve";
    private final EchoGraveConfig config;
    private final GraveStore graves = new GraveStore();
    private final BackTeleportState backTeleportState = new BackTeleportState();
    private final ExecutorService webhookExecutor = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "EchoGrave Webhook"); t.setDaemon(true); return t;
    });

    public EchoGrave(EchoGraveConfig config) { this.config = Objects.requireNonNull(config); }
    public EchoGraveConfig config() { return config; }
    public GraveStore graves() { return graves; }
    public BackTeleportState backTeleportState() { return backTeleportState; }

    public GraveRecord recordDeath(String playerId, String playerName, String deathCause, Position pos, int xpAmount, List<String> itemIds) {
        GraveRecord grave = new GraveRecord(UUID.randomUUID(), playerId, playerName, deathCause, pos, Instant.now(),
                config.storeXp() ? xpAmount : 0, List.copyOf(itemIds), Set.of(playerId), config.decaySeconds());
        graves.add(grave);
        backTeleportState.rememberDeath(playerId, pos);
        if (config.webhookEnabled() && !config.webhookUrl().isBlank()) postWebhookAsync(grave);
        return grave;
    }

    public Optional<GraveRecord> nearestActiveGrave(String playerId, Position from) { return graves.nearestActiveFor(playerId, from); }
    public String compassText(String playerId, Position from) {
        return nearestActiveGrave(playerId, from).map(g -> {
            double distance = from.distanceTo(g.position());
            return String.format(Locale.ROOT, "%s blocks %s", Math.round(distance), from.directionTo(g.position()));
        }).orElse("No active grave");
    }

    private void postWebhookAsync(GraveRecord grave) {
        webhookExecutor.submit(() -> {
            try {
                String json = WebhookPayload.from(grave).toJson();
                HttpRequest request = HttpRequest.newBuilder(URI.create(config.webhookUrl()))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json)).timeout(java.time.Duration.ofSeconds(5)).build();
                HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.discarding());
            } catch (RuntimeException ignored) { }
        });
    }
}
