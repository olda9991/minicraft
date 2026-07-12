@echo off
setlocal enabledelayedexpansion
title MiniCraft v5.0 Installer

echo =================================
echo    MiniCraft v5.0 Installer
echo =================================
echo.
echo Choose an option:
echo   1) Install Java 21 + MiniCraft (auto)
echo   2) Install MiniCraft only
echo   3) Install Java 21 only
echo   4) Run MiniCraft
echo   5) Exit
echo.
set /p choice="Enter choice [1-5]: "

if "%choice%"=="1" goto full_install
if "%choice%"=="2" goto download
if "%choice%"=="3" goto install_java
if "%choice%"=="4" goto run
if "%choice%"=="5" exit /b 0

:full_install
call :install_java
call :download
call :run
goto :eof

:install_java
echo.
echo Installing Java 21...
where java >nul 2>nul
if %ERRORLEVEL% equ 0 (
    java -version 2>&1 | findstr "21" >nul
    if %ERRORLEVEL% equ 0 (
        echo Java 21 already installed!
        goto :eof
    )
)

REM Try winget first (built into Win 10/11)
where winget >nul 2>nul
if %ERRORLEVEL% equ 0 (
    echo Using winget...
    winget install EclipseAdoptium.Temurin.21.JDK --silent --accept-package-agreements 2>nul
    if %ERRORLEVEL% equ 0 (
        echo Java 21 installed via winget!
        set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.2.13-hotspot"
        setx JAVA_HOME "!JAVA_HOME!" >nul
        set "PATH=!JAVA_HOME!\bin;%PATH%"
        goto :eof
    )
)

REM Try chocolatey
where choco >nul 2>nul
if %ERRORLEVEL% equ 0 (
    echo Using Chocolatey...
    choco install temurin21 -y 2>nul
    if %ERRORLEVEL% equ 0 (
        echo Java 21 installed via Chocolatey!
        goto :eof
    )
)

REM Try scoop
where scoop >nul 2>nul
if %ERRORLEVEL% equ 0 (
    echo Using Scoop...
    scoop bucket add java 2>nul
    scoop install temurin21-jdk 2>nul
    if %ERRORLEVEL% equ 0 (
        echo Java 21 installed via Scoop!
        goto :eof
    )
)

REM Fallback: download with PowerShell
echo Downloading JDK with PowerShell...
powershell -Command "$url='https://api.adoptium.net/v3/binary/latest/21/ga/windows/x64/jdk/hotspot/normal/eclipse'; $out='%TEMP%\jdk21.msi'; Invoke-WebRequest -Uri $url -OutFile $out; Start-Process msiexec.exe -Wait -ArgumentList '/i',$out,'/quiet','/norestart','INSTALLDIR=C:\Program Files\Eclipse Adoptium\jdk-21.0.2.13-hotspot'; Remove-Item $out" 2>nul
if exist "C:\Program Files\Eclipse Adoptium\jdk-21.0.2.13-hotspot\bin\java.exe" (
    echo Java 21 installed!
    set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.2.13-hotspot"
    setx JAVA_HOME "!JAVA_HOME!" >nul
    set "PATH=!JAVA_HOME!\bin;%PATH%"
    goto :eof
)

echo.
echo Could not auto-install Java.
echo Please install manually: https://adoptium.net/
pause
goto :eof

:download
echo.
echo Downloading MiniCraft...
mkdir %USERPROFILE%\minicraft 2>nul
cd /d %USERPROFILE%\minicraft
powershell -Command "try { Invoke-WebRequest -Uri 'https://github.com/olda9991/minicraft/releases/latest/download/MiniCraft.jar' -OutFile 'MiniCraft.jar' } catch { Write-Host 'Release JAR not found, cloning from git...'; git clone https://github.com/olda9991/minicraft.git . 2>nul }" 2>nul
if exist MiniCraft.jar (
    echo MiniCraft.jar downloaded to %USERPROFILE%\minicraft\
    echo Double-click MiniCraft.jar to play!
) else (
    echo Download failed. Get it from:
    echo https://github.com/olda9991/minicraft/releases
)
goto :eof

:run
echo.
echo Starting MiniCraft...
cd /d %USERPROFILE%\minicraft 2>nul
if exist MiniCraft.jar (
    start javaw -jar MiniCraft.jar
) else (
    echo MiniCraft.jar not found in %USERPROFILE%\minicraft\
    echo Run option 1 or 2 first.
)
goto :eof
