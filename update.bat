@echo off
setlocal enabledelayedexpansion
echo === MiniCraft Updater ===

REM Download source
echo Downloading...
powershell -Command "Invoke-WebRequest -Uri 'https://raw.githubusercontent.com/olda9991/minicraft/main/src/MiniCraft.java' -OutFile 'mini_temp.java'" 2>nul
if not exist mini_temp.java (
    echo ERROR: Download failed! Check your internet connection.
    pause
    exit /b 1
)

REM Get SHA and prepend
echo Getting latest version...
powershell -Command "$sha=(Invoke-RestMethod 'https://api.github.com/repos/olda9991/minicraft/commits/main').sha.Substring(0,8); $code='//sha:'+$sha+[Environment]::NewLine+[System.IO.File]::ReadAllText('mini_temp.java'); [System.IO.File]::WriteAllText('src\MiniCraft.java',$code); Write-Host 'SHA: '$sha"

REM Create build dir
if not exist build mkdir build

REM Find javac
set JAVAC=
for /f "tokens=*" %%i in ('where javac 2^>nul') do set JAVAC=%%i
if "%JAVAC%"=="" (
    if defined JAVA_HOME set "JAVAC=%JAVA_HOME%\bin\javac.exe"
)
if "%JAVAC%"=="" (
    if exist "C:\Program Files\Eclipse Adoptium\jdk-21.0.2.13-hotspot\bin\javac.exe" set "JAVAC=C:\Program Files\Eclipse Adoptium\jdk-21.0.2.13-hotspot\bin\javac.exe"
)
if "%JAVAC%"=="" (
    for /d %%d in ("C:\Program Files\Java\jdk*") do if exist "%%d\bin\javac.exe" set "JAVAC=%%d\bin\javac.exe"
)
if "%JAVAC%"=="" (
    echo ERROR: javac not found! Install Java JDK 21 from https://adoptium.net/
    echo Make sure to select "Set JAVA_HOME" during installation.
    del mini_temp.java 2>nul
    pause
    exit /b 1
)

echo Using javac: %JAVAC%
echo Compiling...

rmdir /s /q build 2>nul
mkdir build
"%JAVAC%" -d build src\MiniCraft.java
if %ERRORLEVEL% neq 0 (
    echo COMPILE FAILED! Check the errors above.
    del mini_temp.java 2>nul
    pause
    exit /b 1
)

del mini_temp.java 2>nul

REM Rebuild JAR
echo Rebuilding JAR...
if exist manifest.txt (
    jar cfm MiniCraft.jar manifest.txt -C build .
    if %ERRORLEVEL% equ 0 (
        echo JAR rebuilt!
    ) else (
        echo JAR rebuild skipped
    )
)

echo ===============================
echo   Update complete!
echo   Run: run.bat
echo ===============================
pause
