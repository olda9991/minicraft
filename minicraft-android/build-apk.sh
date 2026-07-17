#!/bin/bash
# build-apk.sh - Build MiniCraft Android APK locally or with Docker
# Requires Android SDK (command line tools) or Android Studio

set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR"

# Check for Android SDK
if [ -z "$ANDROID_SDK_ROOT" ] && [ -z "$ANDROID_HOME" ]; then
    echo "ERROR: ANDROID_SDK_ROOT or ANDROID_HOME not set."
    echo "Install Android Studio or command line tools:"
    echo "  https://developer.android.com/studio#command-tools"
    echo "Then set: export ANDROID_SDK_ROOT=/path/to/android-sdk"
    exit 1
fi

SDK="${ANDROID_SDK_ROOT:-$ANDROID_HOME}"
GRADLE="$SDK/gradle/gradle-8.2/bin/gradle"

# Fallback to local gradle wrapper
if [ ! -x "$GRADLE" ]; then
    if [ -f ./gradlew ]; then
        GRADLE="./gradlew"
    else
        echo "Downloading Gradle wrapper..."
        curl -L https://services.gradle.org/distributions/gradle-8.2-bin.zip -o /tmp/gradle.zip
        unzip -q /tmp/gradle.zip -d /tmp/gradle
        GRADLE="/tmp/gradle/gradle-8.2/bin/gradle"
    fi
fi

echo "=== MiniCraft Android Build ==="
echo "SDK: $SDK"
echo "Gradle: $GRADLE"

# Build debug APK
$GRADLE assembleDebug

echo ""
echo "APK built successfully!"
echo "Output: app/build/outputs/apk/debug/app-debug.apk"
echo ""
echo "Install to connected device:"
echo "  adb install app/build/outputs/apk/debug/app-debug.apk"
