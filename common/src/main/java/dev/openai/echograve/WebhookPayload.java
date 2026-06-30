package dev.openai.echograve;
public record WebhookPayload(String playerName, String deathCause, double x, double y, double z, String dimension, String timestamp, int xpAmount) {
 public static WebhookPayload from(GraveRecord g){ return new WebhookPayload(g.ownerName(), g.deathCause(), g.position().x(), g.position().y(), g.position().z(), g.position().dimension(), g.createdAt().toString(), g.xpAmount()); }
 public String toJson(){ return "{\"playerName\":\""+esc(playerName)+"\",\"deathCause\":\""+esc(deathCause)+"\",\"x\":"+x+",\"y\":"+y+",\"z\":"+z+",\"dimension\":\""+esc(dimension)+"\",\"timestamp\":\""+esc(timestamp)+"\",\"xpAmount\":"+xpAmount+"}"; }
 private static String esc(String s){ return s.replace("\\","\\\\").replace("\"","\\\""); }
}
