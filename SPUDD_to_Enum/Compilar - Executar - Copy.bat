@setlocal enableextensions enabledelayedexpansion
@echo on
cls
javac Main.java
@echo off
pause
cls
java Main C:\Users\Daniel\Downloads\RS-MDP\Testes\SysAdmin\SysAdmin_10.dat
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