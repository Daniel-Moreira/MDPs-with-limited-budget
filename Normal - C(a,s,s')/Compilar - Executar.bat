@setlocal enableextensions enabledelayedexpansion
@echo on
cls
javac Main.java
@echo off
pause
cls
java Main augmented_MDP_1.net
pause
endlocal
cls
del *.class
cd Grafo
del *.class
cd..
cd Leitura
del *.class
cd..
cd Solvers
del *.class
cd..
cls