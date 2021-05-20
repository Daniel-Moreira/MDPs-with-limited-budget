@setlocal enableextensions enabledelayedexpansion
@echo on
cls
javac Main.java
@echo off
pause
cls
java Main crossing_traffic_inst_mdp__1.spudd
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