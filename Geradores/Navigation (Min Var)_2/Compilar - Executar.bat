@setlocal enableextensions enabledelayedexpansion
@echo on
cls
javac NavigationGen.java
@echo off
pause
cls
java NavigationGen navigation_11.dat 7 7
pause
end local
cls
del *.class
cls