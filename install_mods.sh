#!/bin/bash
# MiniCraft Mod Installer
cd "$(dirname "$0")"
mkdir -p mods

echo "=== MiniCraft Mod Installer ==="
echo "Drop .class files in the 'mods/' folder"
echo "They'll be loaded when MiniCraft starts"
echo ""
echo "Available mods to install:"
echo "  1. MoreBiomes - different grass/sand colors per area"
echo "  2. FastBreak - instant block breaking"
echo "  3. FlyMod - double-tap space to fly"
echo ""
echo "Coming soon: automatic mod downloads from GitHub"
echo ""
echo "To install a mod, download its .class file to the mods/ folder."
