package dev.openai.echograve;
import java.time.*; import java.util.*;
public record GraveRecord(UUID id, String ownerId, String ownerName, String deathCause, Position position,
                          Instant createdAt, int xpAmount, List<String> itemIds, Set<String> trustedPlayers,
                          long decaySeconds) {
    public boolean isDecayed(Instant now) { return !now.isBefore(createdAt.plusSeconds(decaySeconds)); }
    public boolean isWarning(Instant now) { return !isDecayed(now) && !now.isBefore(createdAt.plusSeconds(Math.max(0, decaySeconds-10))); }
    public boolean canOpen(String playerId, Instant now) { return isDecayed(now) || ownerId.equals(playerId) || trustedPlayers.contains(playerId); }
    public GraveRecord trust(String playerId) { var s = new java.util.HashSet<>(trustedPlayers); s.add(playerId); return new GraveRecord(id, ownerId, ownerName, deathCause, position, createdAt, xpAmount, itemIds, Set.copyOf(s), decaySeconds); }
}
