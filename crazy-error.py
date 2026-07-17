#!/usr/bin/env python3
"""
Crazy Error v2.0 - Ultimate Linux crash dialog
Windows-style error reporting for Bazzite/Hyprland
"""

import sys, subprocess, os

def show_zenity(code, msg):
    subprocess.run([
        'zenity', '--error',
        '--title', '☠ FATAL ERROR ☠',
        '--text', f'<span font="monospace" foreground="#FF0000"><b>💥 CRITICAL ERROR 💥</b></span>\n\n<span font="monospace"><b>Error Code:</b> {code}</span>\n\n{msg}\n\n<i>* The application will now terminate.</i>',
        '--width', '500',
        '--height', '250',
        '--icon-name', 'dialog-error'
    ])

def show_kdialog(code, msg):
    subprocess.run([
        'kdialog', '--error',
        f'<h2>💥 CRITICAL ERROR</h2><br><b>Error Code:</b> {code}<br><br>{msg}<br><br><i>* The application will now terminate.</i>',
        '--title', '☠ FATAL ERROR ☠',
        '--icon', 'dialog-error'
    ])

def show_notify(code, msg):
    subprocess.run([
        'notify-send', '-u', 'critical', '-i', 'dialog-error',
        '☠ FATAL ERROR ☠',
        f'{code}\n{msg}\n\n* The application will now terminate.'
    ])
    input("Press Enter to die...")

def show_terminal(code, msg):
    print(f"\n\033[48;5;196m\033[38;5;231m{'':^80}\033[0m")
    print(f"\033[48;5;196m\033[38;5;231m{'☠  FATAL SYSTEM ERROR  ☠':^80}\033[0m")
    print(f"\033[48;5;196m\033[38;5;231m{'':^80}\033[0m")
    print(f"\n\033[38;5;196m\033[1m  Error Code: {code}\033[0m")
    print(f"\033[38;5;220m\n  {msg}\n\033[0m")
    print(f"\033[38;5;33m  * The application will now terminate.\033[0m")
    print(f"\n\033[48;5;196m\033[38;5;231m{'':^80}\033[0m")
    input("")

def main():
    code = sys.argv[1] if len(sys.argv) > 1 else "0x0000007B"
    msg = sys.argv[2] if len(sys.argv) > 2 else "A fatal exception has occurred. The current application will be terminated."
    
    # Play error sound if available
    if os.path.exists("/usr/share/sounds/freedesktop/stereo/dialog-error.oga"):
        subprocess.Popen(["paplay", "/usr/share/sounds/freedesktop/stereo/dialog-error.oga"], 
                        stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
    
    # Try fancy dialogs
    if subprocess.run(['which', 'zenity'], capture_output=True).returncode == 0:
        show_zenity(code, msg)
    elif subprocess.run(['which', 'kdialog'], capture_output=True).returncode == 0:
        show_kdialog(code, msg)
    elif subprocess.run(['which', 'notify-send'], capture_output=True).returncode == 0:
        show_notify(code, msg)
    else:
        show_terminal(code, msg)
    
    print(f"[CrazyError] Application terminated with code {code}")
    sys.exit(1)

if __name__ == "__main__":
    main()
