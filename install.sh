#!/bin/bash
# MiniCraft Installer - detects OS, installs Java 21, sets up the game
set -e

RED='\033[0;31m'; GREEN='\033[0;32m'; YELLOW='\033[1;33m'; CYAN='\033[0;36m'; NC='\033[0m'
echo -e "${GREEN}=================================${NC}"
echo -e "${GREEN}   MiniCraft v5.0 Installer     ${NC}"
echo -e "${GREEN}=================================${NC}"
echo ""

# Detect OS
OS="unknown"
if [[ "$OSTYPE" == "linux-gnu"* ]]; then OS="linux"
elif [[ "$OSTYPE" == "darwin"* ]]; then OS="mac"
elif [[ "$OSTYPE" == "msys" || "$OSTYPE" == "cygwin" ]]; then OS="windows"
fi

echo -e "${CYAN}Detected OS: $OS${NC}"
echo ""

# Menu
echo "Choose an option:"
echo "  1) Install Java 21 + MiniCraft (auto)"
echo "  2) Install Java 21 only"
echo "  3) Download MiniCraft only"
echo "  4) Run MiniCraft"
echo "  5) Exit"
echo ""
read -p "Enter choice [1-5]: " choice

case $choice in
    1|2)
        echo ""
        echo -e "${YELLOW}Installing Java 21...${NC}"
        case $OS in
            linux)
                if command -v apt &>/dev/null; then
                    echo "Detected Debian/Ubuntu"
                    sudo apt update -y && sudo apt install -y openjdk-21-jdk
                elif command -v dnf &>/dev/null; then
                    echo "Detected Fedora/RHEL"
                    sudo dnf install -y java-21-openjdk-devel
                elif command -v pacman &>/dev/null; then
                    echo "Detected Arch"
                    sudo pacman -S --noconfirm jdk21-openjdk
                elif command -v zypper &>/dev/null; then
                    echo "Detected openSUSE"
                    sudo zypper install -y java-21-openjdk-devel
                elif command -v brew &>/dev/null; then
                    echo "Using Homebrew"
                    brew install openjdk@21
                else
                    echo -e "${RED}Could not detect package manager!${NC}"
                    echo "Please install Java 21 manually: https://adoptium.net/"
                    exit 1
                fi
                ;;
            mac)
                if command -v brew &>/dev/null; then
                    brew install openjdk@21
                else
                    echo "Install Homebrew first: /bin/bash -c \"\$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)\""
                    echo "Then: brew install openjdk@21"
                fi
                ;;
            windows)
                echo -e "${YELLOW}On Windows, please install Java 21 manually:${NC}"
                echo "  1. Go to https://adoptium.net/"
                echo "  2. Download the JDK 21 .msi installer"
                echo "  3. Run it and check 'Set JAVA_HOME'"
                echo "  4. Then run install.bat instead"
                exit 0
                ;;
        esac
        echo -e "${GREEN}Java 21 installed!${NC}"
        ;;&
    1|3)
        echo ""
        echo -e "${YELLOW}Downloading MiniCraft...${NC}"
        mkdir -p ~/minicraft
        cd ~/minicraft
        if command -v curl &>/dev/null; then
            curl -sL -o MiniCraft.jar "https://github.com/olda9991/minicraft/releases/latest/download/MiniCraft.jar"
        elif command -v wget &>/dev/null; then
            wget -q -O MiniCraft.jar "https://github.com/olda9991/minicraft/releases/latest/download/MiniCraft.jar"
        fi
        if [ -f MiniCraft.jar ] && [ -s MiniCraft.jar ]; then
            echo -e "${GREEN}MiniCraft downloaded to ~/minicraft/${NC}"
        else
            echo -e "${RED}Download failed! Cloning from git instead...${NC}"
            git clone https://github.com/olda9991/minicraft.git ~/minicraft 2>/dev/null
            cd ~/minicraft && bash update.sh
        fi
        echo -e "${GREEN}Done!${NC}"
        ;;&
    1|3|4)
        echo ""
        echo -e "${YELLOW}Launching MiniCraft...${NC}"
        cd ~/minicraft 2>/dev/null || cd "$(dirname "$0")"
        if [ -f MiniCraft.jar ]; then
            java -jar MiniCraft.jar
        elif [ -f run.sh ]; then
            bash run.sh
        else
            echo -e "${RED}MiniCraft not found! Run option 3 first.${NC}"
        fi
        ;;
    5)
        echo "Bye!"
        exit 0
        ;;
    *)
        echo -e "${RED}Invalid choice!${NC}"
        exit 1
        ;;
esac
