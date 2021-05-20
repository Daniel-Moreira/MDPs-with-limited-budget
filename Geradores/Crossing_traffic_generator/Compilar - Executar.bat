@setlocal enableextensions enabledelayedexpansion
@echo on
cls
javac Main.java
@echo off
pause
cls
java Main 1 3 3 0.3  
pause
end local
cls
del *.class
cls