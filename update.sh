#!/bin/bash
# MiniCraft updater - pulls latest from GitHub, compiles, rebuilds JAR
cd "$(dirname "$0")"

echo "=== MiniCraft Updater ==="

# Get latest SHA
SHA=$(curl -s "https://api.github.com/repos/olda9991/minicraft/commits/main" | python3 -c "import json,sys;print(json.load(sys.stdin)['sha'][:8])")
echo "Latest commit: $SHA"

# Download source
echo "Downloading..."
curl -s "https://raw.githubusercontent.com/olda9991/minicraft/main/src/MiniCraft.java" -o /tmp/mc_update.java

# Write with SHA header
python3 -c "
code = open('/tmp/mc_update.java').read()
open('src/MiniCraft.java','w').write('//sha:$SHA\n' + code)
"

# Compile
echo "Compiling..."
JAVA_HOME=${JAVA_HOME:-/home/linuxbrew/.linuxbrew/opt/openjdk@21}
JAVAC="$JAVA_HOME/bin/javac"
if [ ! -x "$JAVAC" ]; then
    JAVAC=$(which javac 2>/dev/null || find /usr -name javac 2>/dev/null | head -1)
fi
if [ -z "$JAVAC" ] || [ ! -x "$JAVAC" ]; then
    echo "ERROR: javac not found! Install Java JDK 21+"
    exit 1
fi
rm -rf build && mkdir build
"$JAVAC" -d build src/MiniCraft.java 2>&1
if [ $? -ne 0 ]; then
    echo "COMPILE FAILED!"
    exit 1
fi

# Rebuild JAR so run.sh uses latest code
echo "Rebuilding JAR..."
jar cfm MiniCraft.jar manifest.txt -C build .
if [ $? -eq 0 ]; then
    echo "JAR rebuilt!"
else
    echo "JAR build skipped (jar not found), using build/"
fi

echo ""
echo "Run with: ./run.sh"
