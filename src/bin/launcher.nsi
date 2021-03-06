;-------------------------------------------------------------------------------
;
; Jetrix Launcher
; 
; based on the Java Launcher from 
; http://nsis.sf.net/archive/nsisweb.php?page=326
;
;-------------------------------------------------------------------------------

SetCompressor lzma

Name "Jetrix TetriNET Server"
Caption "Jetrix TetriNET Server"
Icon "jetrix.ico"
OutFile "jetrix.exe"

SilentInstall silent
AutoCloseWindow true
ShowInstDetails nevershow

!define ARGS "-Xmx128m -Djava.library.path=lib -jar lib/jetrix-launcher-@version@.jar"

Section ""
  Call GetJRE
  Pop $R0

  ; change for your purpose (-jar etc.)
  StrCpy $0 '"$R0" ${ARGS}'

  SetOutPath $EXEDIR
  Exec $0
SectionEnd


;
; Find JRE (javaw.exe)
; 1 - in JAVA_HOME environment variable
; 2 - in the registry
; 3 - assume javaw.exe in current dir or PATH

Function GetJRE

  Push $R0
  Push $R1

  ClearErrors
  ReadEnvStr $R0 "JAVA_HOME"
  StrCpy $R0 "$R0\bin\javaw.exe"
  IfErrors 0 JreFound

  ClearErrors
  ReadRegStr $R1 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
  ReadRegStr $R0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$R1" "JavaHome"
  StrCpy $R0 "$R0\bin\javaw.exe"
  IfErrors 0 JreFound

  ClearErrors
  ReadRegStr $R1 HKLM "SOFTWARE\JavaSoft\Java Development Kit" "CurrentVersion"
  ReadRegStr $R0 HKLM "SOFTWARE\JavaSoft\Java Development Kit\$R1" "JavaHome"
  StrCpy $R0 "$R0\bin\javaw.exe"
  IfErrors 0 JreFound
  
  MessageBox MB_ICONEXCLAMATION|MB_YESNO \
          'Could not find a Java Runtime Environment installed on your computer. \
          $\nWithout it you cannot run Jetrix. \
          $\n$\nWould you like to visit the Java website to download it?' \
          IDNO +2
  ExecShell open "http://java.com"
  Quit
        
 JreFound:
  Pop $R1
  Exch $R0

FunctionEnd
