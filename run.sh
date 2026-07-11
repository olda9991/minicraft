#!/bin/bash
cd "$(dirname "$0")"
export PATH="/home/linuxbrew/.linuxbrew/opt/openjdk@21/bin:$PATH"
export _JAVA_AWT_WM_NONREPARENTING=1
java -cp build MiniCraft
