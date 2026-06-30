# EchoGrave

EchoGrave is an English Minecraft multiloader-style grave mod project for Fabric and NeoForge targets: 1.20.1, 1.20.4, and 1.21.1. Mod ID: `echograve`; Java package: `dev.openai.echograve`.

## Features
- Stores a player's inventory and configurable XP amount in a grave record on death instead of dropping it immediately.
- Remembers last death locations for `/back` and applies a configurable cooldown with an admin bypass hook.
- Grave placement candidates prefer the death block and then nearby safe offsets when the original block cannot be replaced.
- Grave block concept with hologram data: player head/skin, player name, and death cause.
- Configurable decay timer, default 30 minutes; warning state begins 10 seconds before expiry; contents release after decay.
- Owner-only access before decay; `/grave trust <player>` design grants extra access; decayed graves are public.
- Craftable Grave Compass recipe and common logic for nearest active grave direction/distance.
- Optional asynchronous HTTP JSON webhook containing `playerName`, `deathCause`, `x`, `y`, `z`, `dimension`, `timestamp`, and `xpAmount`.
- Config fields for decay time, `/back` cooldown, webhook URL/toggle, XP storage, and Grave Compass crafting.
- English (`en_us`) and German (`de_de`) localizations are included; gameplay copy is English by default.

## Build

```bash
./gradlew build -PmcVersion=1.20.1
./gradlew build -PmcVersion=1.20.4
./gradlew build -PmcVersion=1.21.1
./gradlew buildAllVersions packageDistribution
```

Compiled artifacts are generated in `build-output/`. Binary jars and zips are intentionally ignored by git so code review stays text-only; run the packaging command above to recreate them.

## Config example

```json
{
  "decaySeconds": 1800,
  "backCooldownSeconds": 60,
  "webhookEnabled": false,
  "webhookUrl": "https://example.invalid/webhook",
  "storeXp": true,
  "graveCompassCraftable": true
}
```
