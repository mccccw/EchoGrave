package dev.openai.echograve;
import java.time.*; import java.util.*; import java.util.concurrent.*;
public final class GraveStore {
 private final ConcurrentMap<UUID, GraveRecord> graves = new ConcurrentHashMap<>();
 public void add(GraveRecord grave){ graves.put(grave.id(), grave); }
 public Optional<GraveRecord> get(UUID id){ return Optional.ofNullable(graves.get(id)); }
 public void trust(UUID graveId, String playerId){ graves.computeIfPresent(graveId,(id,g)->g.trust(playerId)); }
 public List<GraveRecord> active(Instant now){ return graves.values().stream().filter(g->!g.isDecayed(now)).toList(); }
 public Optional<GraveRecord> nearestActiveFor(String playerId, Position from){ Instant now=Instant.now(); return graves.values().stream().filter(g->!g.isDecayed(now)).filter(g->g.ownerId().equals(playerId)||g.trustedPlayers().contains(playerId)).min(Comparator.comparingDouble(g->from.distanceTo(g.position()))); }
}
