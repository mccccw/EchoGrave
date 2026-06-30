# EchoGrave

EchoGrave is a lightweight Minecraft grave mod for Fabric and NeoForge. When a player dies, their items and optional XP are saved in a grave instead of being scattered across the world. The mod also includes a Grave Compass, `/back` support, grave trust access, configurable decay, and optional webhook notifications for server automation.

## Features

### Graves on death

- Stores the player's inventory in a grave at the death location.
- Remembers the death position for `/back` teleport support.
- Tries the death block first, then nearby offsets if the grave cannot be placed there.
- Supports owner-only grave access before decay.
- Supports trusted players through the grave trust design.
- Decayed graves become public and release their contents normally.

### Decay and warning state

- Default grave lifetime: 30 minutes.
- The warning state begins 10 seconds before expiry.
- Server owners can change the decay duration in config.

### Grave Compass

- Adds a craftable Grave Compass recipe.
- Points players toward their nearest active grave using direction and distance helper text.
- Crafting can be disabled in config.

### `/back` support

- Stores each player's last death location.
- Configurable cooldown, default 60 seconds.
- Includes an admin bypass hook for permission integrations.

### Optional webhooks

EchoGrave can send an asynchronous HTTP POST when a player dies. This never blocks the server thread. Payload fields include:

- `playerName`
- `deathCause`
- `x`, `y`, `z`
- `dimension`
- `timestamp`
- `xpAmount`

### Localization

- English: `en_us`
- German: `de_de`

## Supported targets

- Minecraft 1.20.1
- Minecraft 1.20.4
- Minecraft 1.21.1
- Fabric
- NeoForge

## Configuration example

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

## Building from source

```bash
./gradlew build -PmcVersion=1.20.1
./gradlew build -PmcVersion=1.20.4
./gradlew build -PmcVersion=1.21.1
./gradlew buildAllVersions packageDistribution
```

Generated jars are written to `build-output/`.
