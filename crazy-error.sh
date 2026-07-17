#!/bin/bash
# Crazy Error for Linux - dramatic crash dialog like Windows
# Usage: ./crazy-error.sh [error-code] [message]

ERR_CODE="${1:-0x0000007B}"
ERR_MSG="${2:-A fatal exception has occurred. The current application will be terminated.}"

# Check what dialog tool is available
if command -v zenity &>/dev/null; then
    # GTK Zenity style
    zenity --error \
        --title="FATAL ERROR" \
        --text="<b><big>💥 CRITICAL ERROR</big></b>\n\n<b>Error Code:</b> $ERR_CODE\n\n$ERR_MSG\n\n<i>* Press any key to terminate...</i>" \
        --width=450 \
        --height=200 \
        --icon-name=dialog-error \
        --ok-label="ABORT"
elif command -v kdialog &>/dev/null; then
    # KDE KDialog style
    kdialog --error "<h2>💥 CRITICAL ERROR</h2><br><b>Error Code:</b> $ERR_CODE<br><br>$ERR_MSG<br><br><i>* Press any key to terminate...</i>" \
        --title "FATAL ERROR" \
        --icon dialog-error
elif command -v notify-send &>/dev/null; then
    # Notify-send fallback
    notify-send -u critical -i dialog-error "FATAL ERROR 💥" "$ERR_CODE\n$ERR_MSG"
    read -p "Press Enter to terminate..."
else
    # Terminal fallback with ANSI colors
    echo -e "\033[41m\033[37m\033[1m                                                                                \033[0m"
    echo -e "\033[41m\033[37m\033[1m  💥  FATAL SYSTEM ERROR                                                        \033[0m"
    echo -e "\033[41m\033[37m\033[1m                                                                                \033[0m"
    echo -e "\033[31m\033[1m\n  Error Code: $ERR_CODE\033[0m"
    echo -e "\033[33m\n  $ERR_MSG\n\033[0m"
    echo -e "\033[36m  * Press any key to terminate...\033[0m"
    read -n1 -s
fi

echo "[CrazyError] Application terminated with code $ERR_CODE"
exit 1
