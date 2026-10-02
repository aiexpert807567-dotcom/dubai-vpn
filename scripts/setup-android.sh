#!/usr/bin/env bash
# Dubai VPN - Android toolchain setup (LOCAL/CODESPACES only)
set -euo pipefail

TOOLS="$HOME/tools"
JDK_DIR="$TOOLS/jdk17"
SDK_DIR="$HOME/android-sdk"
GRADLE_VER="9.4.1"
GRADLE_DIR="$TOOLS/gradle-$GRADLE_VER"
CMDLINE_VER="14742923"

mkdir -p "$TOOLS" "$SDK_DIR/cmdline-tools"

case "$(uname -m)" in
  x86_64)  ARCH="x64" ;;
  aarch64) ARCH="aarch64" ;;
  *) echo "Unsupported CPU: $(uname -m)"; exit 1 ;;
esac

# ---- JDK 17 ----
if [ ! -x "$JDK_DIR/bin/java" ]; then
  echo ">> Downloading JDK 17 (Temurin)..."
  curl -fL "https://api.adoptium.net/v3/binary/latest/17/ga/linux/${ARCH}/jdk/hotspot/normal/eclipse" -o /tmp/jdk17.tar.gz
  rm -rf "$JDK_DIR"
  mkdir -p "$JDK_DIR"
  tar -xzf /tmp/jdk17.tar.gz -C "$JDK_DIR" --strip-components=1
  rm -f /tmp/jdk17.tar.gz
fi

# ---- Android command-line tools ----
if [ ! -x "$SDK_DIR/cmdline-tools/latest/bin/sdkmanager" ]; then
  echo ">> Downloading Android command-line tools..."
  curl -fL "https://dl.google.com/android/repository/commandlinetools-linux-${CMDLINE_VER}_latest.zip" -o /tmp/cmdline-tools.zip
  rm -rf /tmp/cmdline-tools
  unzip -q /tmp/cmdline-tools.zip -d /tmp
  rm -rf "$SDK_DIR/cmdline-tools/latest"
  mv /tmp/cmdline-tools "$SDK_DIR/cmdline-tools/latest"
  rm -f /tmp/cmdline-tools.zip
fi

# ---- Gradle ----
if [ ! -x "$GRADLE_DIR/bin/gradle" ]; then
  echo ">> Downloading Gradle $GRADLE_VER..."
  curl -fL "https://services.gradle.org/distributions/gradle-${GRADLE_VER}-bin.zip" -o /tmp/gradle.zip
  unzip -q /tmp/gradle.zip -d "$TOOLS"
  rm -f /tmp/gradle.zip
fi

# ---- Load environment, accept licenses, install SDK packages ----
source "$(dirname "$0")/android-env.sh"

echo ">> Accepting Android SDK licenses..."
yes | sdkmanager --licenses >/dev/null || true

echo ">> Installing SDK packages..."
sdkmanager "platform-tools" "platforms;android-36" "build-tools;36.0.0"

echo
echo "== Setup complete =="
java -version
gradle --version | head -n 8
sdkmanager --list_installed | head -n 12
