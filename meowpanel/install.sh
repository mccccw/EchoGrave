#!/usr/bin/env bash
# MEOWPANEL 0.1.0 verified bootstrap installer.
set -Eeuo pipefail
RELEASE_SHA256="3622737fdee945aac342fa51ca2139fdedd8b51ef1dd8d775fa0c5233605d5ad"
RELEASE_URL="https://raw.githubusercontent.com/mccccw/EchoGrave/da231595149d89ea6a6d8168b2b408bf38c93632/meowpanel/release/meowpanel-0.1.0-local.tar.gz.b64"
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
