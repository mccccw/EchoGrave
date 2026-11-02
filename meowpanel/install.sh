#!/usr/bin/env bash
# MEOWPANEL 0.1.0 verified bootstrap installer.
set -Eeuo pipefail
RELEASE_SHA256="18db0f6ab209d0a2112c6c2050f71202463366e4cc805c9c6c336c734c91c392"
RELEASE_URL="https://raw.githubusercontent.com/mccccw/EchoGrave/22c501e1d19abc8e5530d360d64da6c4cb30f7a9/meowpanel/release/meowpanel-0.1.0-local.tar.gz.b64"
release_log(){ printf '[MEOWPANEL bootstrap] %s\n' "$*"; }
[ "${EUID}" -eq 0 ] || { release_log "Run with sudo bash"; exit 1; }
command -v apt-get >/dev/null || { release_log "Raspberry Pi OS / Debian with apt-get is required"; exit 1; }
apt-get update
DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends ca-certificates curl
release_temp="$(mktemp -d)"
trap 'rm -rf "$release_temp"' EXIT
release_log "Downloading verified MEOWPANEL release artifact"
curl --fail --location --retry 3 --output "$release_temp/release.b64" "$RELEASE_URL"
base64 --decode "$release_temp/release.b64" > "$release_temp/release.tar.gz"
printf '%s  %s\n' "$RELEASE_SHA256" "$release_temp/release.tar.gz" | sha256sum --check --status || { release_log "Release checksum verification failed"; exit 1; }
tar --list --gzip --file "$release_temp/release.tar.gz" | awk '/(^\/|(^|\/)\.\.($|\/))/{bad=1} END{exit bad}' || { release_log "Release archive contains an unsafe path"; exit 1; }
install -d -m 0755 /opt/meowpanel
tar --extract --gzip --file "$release_temp/release.tar.gz" --directory /opt/meowpanel
exec bash /opt/meowpanel/install.sh
