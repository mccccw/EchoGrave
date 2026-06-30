package dev.openai.echograve;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/** Tracks last-death locations and /back cooldowns without depending on a specific loader API. */
public final class BackTeleportState {
    private final Map<String, Position> lastDeaths = new ConcurrentHashMap<>();
    private final Map<String, Instant> lastBackUse = new ConcurrentHashMap<>();

    public void rememberDeath(String playerId, Position position) {
        lastDeaths.put(playerId, position);
    }

    public Optional<Position> lastDeath(String playerId) {
        return Optional.ofNullable(lastDeaths.get(playerId));
    }

    public boolean canUseBack(String playerId, Instant now, Duration cooldown, boolean bypassPermission) {
        if (bypassPermission) return true;
        Instant previous = lastBackUse.get(playerId);
        return previous == null || !now.isBefore(previous.plus(cooldown));
    }

    public void markBackUsed(String playerId, Instant now) {
        lastBackUse.put(playerId, now);
    }
}
