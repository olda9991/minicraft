@echo off
title MiniCraft
REM MiniCraft Windows Launcher
REM Place MiniCraft.jar + textures/ + music/ + sounds/ in same folder

where java >nul 2>nul
if %ERRORLEVEL% neq 0 (
    echo Java not found! Install from https://adoptium.net/
    echo.
    echo After installing Java 21, run this file again.
    pause
    exit /b 1
)

REM If JAR exists, run it
if exist MiniCraft.jar (
    java -jar MiniCraft.jar
) else (
    REM Compile from source
    if not exist build\ mkdir build
    if exist src\MiniCraft.java (
        javac -d build src\MiniCraft.java
        if %ERRORLEVEL% neq 0 (
            echo Compile failed! Make sure Java JDK is installed.
            pause
            exit /b 1
        )
    )
    java -cp build MiniCraft
)
pause
