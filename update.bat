@echo off
REM MiniCraft Updater for Windows
echo === MiniCraft Updater ===

REM Download source
echo Downloading...
curl -s -o mini_temp.java "https://raw.githubusercontent.com/olda9991/minicraft/main/src/MiniCraft.java"
if %ERRORLEVEL% neq 0 (
    echo ERROR: curl not found! Install curl or use PowerShell.
    pause
    exit /b 1
)

REM Get latest SHA and add header (PowerShell)
powershell -Command "$sha=(Invoke-RestMethod https://api.github.com/repos/olda9991/minicraft/commits/main).sha.Substring(0,8); echo PREPENDING sha //sha:$sha; $code='//sha:'+$sha+'`n'+[System.IO.File]::ReadAllText('mini_temp.java'); [System.IO.File]::WriteAllText('src\MiniCraft.java',$code)"

REM Find javac
for %%i in (javac.exe) do set JAVAC=%%~$PATH:i
if "%JAVAC%"=="" (
    set JAVAC="%JAVA_HOME%\bin\javac.exe"
)
if not exist %JAVAC% (
    echo ERROR: javac not found! Make sure Java JDK 21+ is installed.
    pause
    exit /b 1
)

REM Compile
echo Compiling...
%JAVAC% -d build src\MiniCraft.java
if %ERRORLEVEL% neq 0 (
    echo COMPILE FAILED!
    pause
    exit /b 1
)

del mini_temp.java
echo Compile OK!
echo Run with: run.bat
pause
