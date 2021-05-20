@setlocal enableextensions enabledelayedexpansion
@echo on
cls
javac Main.java
@echo off
pause
cls
for /f "tokens=*" %%a in ('dir C:\Users\Daniel\Downloads\RS-MDP\Testes\SysAdmin\*.net /b') do (
    echo ---------------------------
    echo filename: %%a
    java Main C:/Users/Daniel/Downloads/RS-MDP/Testes/SysAdmin/%%a
    cls
)
pause
endlocal
cls
del *.class
cd ADD
del *.class
cd..
cd Leitura
del *.class
cd..
cd Solvers
del *.class
cd..
cd Domain
del *.class
cd..
cls