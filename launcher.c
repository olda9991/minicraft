#include <windows.h>
#include <stdio.h>
#include <stdlib.h>

int WINAPI WinMain(HINSTANCE h, HINSTANCE p, LPSTR cmd, int show) {
    STARTUPINFO si = {sizeof(si)};
    PROCESS_INFORMATION pi;
    char path[MAX_PATH];
    char cmdline[MAX_PATH*2];
    
    GetModuleFileName(NULL, path, MAX_PATH);
    char *slash = strrchr(path, '\\');
    if(slash) *slash = '\0';
    
    // Try JAVA_HOME first, then PATH
    snprintf(cmdline, sizeof(cmdline), 
        "cmd /c \"cd /d %s && (java -jar MiniCraft.jar || ("
        "if exist \"%%JAVA_HOME%%\\bin\\java.exe\" \"%%JAVA_HOME%%\\bin\\java.exe\" -jar MiniCraft.jar || "
        "echo Java not found! Install from https://adoptium.net/ && pause))\"", path);
    
    CreateProcess(NULL, cmdline, NULL, NULL, FALSE, 
        CREATE_NO_WINDOW, NULL, NULL, &si, &pi);
    WaitForSingleObject(pi.hProcess, INFINITE);
    CloseHandle(pi.hProcess);
    CloseHandle(pi.hThread);
    return 0;
}

int main() { return WinMain(GetModuleHandle(NULL), NULL, GetCommandLine(), SW_SHOW); }
