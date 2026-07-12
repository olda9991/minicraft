@echo off
setlocal enabledelayedexpansion
title MiniCraft v5.0 Installer

echo =================================
echo    MiniCraft v5.0 Installer
echo =================================
echo.

echo This installer will set up MiniCraft on your Windows PC.
echo Java 21 is required (free from https://adoptium.net/)
echo.
echo Choose an option:
echo   1) Check Java + Download MiniCraft
echo   2) Download MiniCraft only
echo   3) Run MiniCraft
echo   4) Install Java 21 (opens browser)
echo   5) Exit
echo.
set /p choice="Enter choice [1-5]: "

if "%choice%"=="1" goto check_java
if "%choice%"=="2" goto download
if "%choice%"=="3" goto run
if "%choice%"=="4" goto install_java
if "%choice%"=="5" exit /b 0
echo Invalid choice!
pause
exit /b 1

:check_java
echo.
echo Checking for Java...
where java >nul 2>nul
if %ERRORLEVEL% neq 0 (
    echo Java not found! Install Java 21 from:
    echo https://adoptium.net/
    start https://adoptium.net/
    echo.
    echo After installing, run this installer again.
    pause
    exit /b 1
)
java -version 2>&1 | findstr "21" >nul
if %ERRORLEVEL% neq 0 (
    echo Java found but might not be version 21!
    echo Current version:
    java -version 2>&1
)
echo Java 21 OK!

:download
echo.
echo Downloading MiniCraft...
mkdir %USERPROFILE%\minicraft 2>nul
cd /d %USERPROFILE%\minicraft
powershell -Command "Invoke-WebRequest -Uri 'https://github.com/olda9991/minicraft/releases/latest/download/MiniCraft.jar' -OutFile 'MiniCraft.jar'" 2>nul
if exist MiniCraft.jar (
    echo MiniCraft downloaded!
    echo You can now double-click MiniCraft.jar to play!
) else (
    echo Download failed! Download manually from:
    echo https://github.com/olda9991/minicraft/releases
)
goto menu_end

:run
cd /d %USERPROFILE%\minicraft 2>nul
if exist MiniCraft.jar (
    echo Starting MiniCraft...
    start javaw -jar MiniCraft.jar
) else (
    echo MiniCraft not found! Run option 2 first.
)
goto menu_end

:install_java
echo Opening https://adoptium.net/ ...
start https://adoptium.net/
echo Download the JDK 21 .msi, run it, and check "Set JAVA_HOME"

:menu_end
echo.
pause
