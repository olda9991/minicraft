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

# Crazy Error handler - shows Windows-style error on crash
CRAZY_ERROR="$(dirname "$0")/crazy-error.sh"

# Prefer JAR, fallback to compiled classes, auto-compile if needed
if [ -f MiniCraft.jar ]; then
    "$JAVA" -jar MiniCraft.jar "$@"
    EXIT=$?
elif [ -f build/MiniCraft.class ]; then
    "$JAVA" -cp build MiniCraft "$@"
    EXIT=$?
elif [ -f src/MiniCraft.java ]; then
    echo "Compiling..."
    JAVAC=$(which javac 2>/dev/null)
    [ -z "$JAVAC" ] && JAVAC="${JAVA%/java}/javac"
    if [ -x "$JAVAC" ]; then
        mkdir -p build
        if "$JAVAC" -d build src/MiniCraft.java; then
            "$JAVA" -cp build MiniCraft "$@"
            EXIT=$?
        else
            EXIT=1
        fi
    else
        echo "ERROR: javac not found! Install Java JDK 21+"
        exit 1
    fi
else
    echo "ERROR: No MiniCraft.jar, build/, or src/ found!"
    exit 1
fi

# Show Crazy Error if game crashed (non-zero exit and not user-killed)
if [ $EXIT -ne 0 ] && [ -x "$CRAZY_ERROR" ]; then
    "$CRAZY_ERROR" "0xC0000005" "MiniCraft has encountered a fatal exception and needs to close. We are sorry for the inconvenience.\n\nIf you were in the middle of something, the progress you were working on might be lost."
fi

exit $EXIT
