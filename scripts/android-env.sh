#!/usr/bin/env bash
# Dubai VPN - Android environment (LOCAL/CODESPACES)
# Usage: source scripts/android-env.sh
export JAVA_HOME="$HOME/tools/jdk17"
export ANDROID_HOME="$HOME/android-sdk"
export ANDROID_SDK_ROOT="$ANDROID_HOME"
export PATH="$JAVA_HOME/bin:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools:$HOME/tools/gradle-9.4.1/bin:$PATH"
echo "Android env ready. JAVA_HOME=$JAVA_HOME"
