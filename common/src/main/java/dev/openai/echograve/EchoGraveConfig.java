package dev.openai.echograve;

public record EchoGraveConfig(long decaySeconds, long backCooldownSeconds, boolean webhookEnabled,
                              String webhookUrl, boolean storeXp, boolean graveCompassCraftable) {
    public static EchoGraveConfig defaults() { return new EchoGraveConfig(1800, 60, false, "", true, true); }
}
