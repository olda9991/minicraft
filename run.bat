@echo off
REM MiniCraft Launcher for Windows
REM Requires Java 21+ from https://adoptium.net/

setlocal
set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.2.13-hotspot"
set "PATH=%JAVA_HOME%\bin;%PATH%"

where java >nul 2>nul
if %ERRORLEVEL% neq 0 (
    echo Java not found! Install Java 21 from https://adoptium.net/
    pause
    exit /b 1
)

java -cp build MiniCraft
pause
