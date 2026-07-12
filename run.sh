#!/bin/bash
cd "$(dirname "$0")"

# Find Java
JAVA=$(which java 2>/dev/null)
if [ -z "$JAVA" ] && [ -x "/home/linuxbrew/.linuxbrew/opt/openjdk@21/bin/java" ]; then
    JAVA="/home/linuxbrew/.linuxbrew/opt/openjdk@21/bin/java"
fi
if [ -z "$JAVA" ]; then
    echo "ERROR: Java not found! Install Java 21+ from https://adoptium.net/"
    exit 1
fi

export _JAVA_AWT_WM_NONREPARENTING=1

# Show notification only if fyi is available
if command -v fyi &>/dev/null; then
    fyi -i "$PWD/icon.png" minicraft 2>/dev/null || true
fi

# Prefer JAR, fallback to compiled classes, auto-compile if needed
if [ -f MiniCraft.jar ]; then
    exec "$JAVA" -jar MiniCraft.jar "$@"
elif [ -f build/MiniCraft.class ]; then
    exec "$JAVA" -cp build MiniCraft "$@"
elif [ -f src/MiniCraft.java ]; then
    echo "Compiling..."
    JAVAC=$(which javac 2>/dev/null)
    [ -z "$JAVAC" ] && JAVAC="${JAVA%/java}/javac"
    if [ -x "$JAVAC" ]; then
        mkdir -p build
        "$JAVAC" -d build src/MiniCraft.java && exec "$JAVA" -cp build MiniCraft "$@"
    fi
    echo "ERROR: javac not found! Install Java JDK 21+"
    exit 1
else
    echo "ERROR: No MiniCraft.jar, build/, or src/ found!"
    exit 1
fi
