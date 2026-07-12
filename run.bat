@echo off
setlocal enabledelayedexpansion
title MiniCraft

REM Find Java
set JAVA=
for /f "tokens=*" %%i in ('where java 2^>nul') do set JAVA=%%i
if "%JAVA%"=="" (
    if defined JAVA_HOME set "JAVA=%JAVA_HOME%\bin\java.exe"
)
if "%JAVA%"=="" (
    if exist "C:\Program Files\Eclipse Adoptium\jdk-21.0.2.13-hotspot\bin\java.exe" (
        set "JAVA=C:\Program Files\Eclipse Adoptium\jdk-21.0.2.13-hotspot\bin\java.exe"
    )
)
if "%JAVA%"=="" (
    echo Java not found! Install Java 21 from https://adoptium.net/
    pause
    exit /b 1
)

REM Prefer JAR, fallback to compiled classes
if exist MiniCraft.jar (
    "%JAVA%" -jar MiniCraft.jar %*
) else if exist build\MiniCraft.class (
    "%JAVA%" -cp build MiniCraft %*
) else (
    echo No MiniCraft.jar or build\ found! Run update.bat first.
    pause
    exit /b 1
)
