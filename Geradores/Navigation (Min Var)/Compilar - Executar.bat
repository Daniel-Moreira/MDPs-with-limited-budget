@setlocal enableextensions enabledelayedexpansion
@echo on
cls
javac NavigationGen.java
@echo off
pause
cls
java NavigationGen navigation_2.dat 20 20
pause
end local
cls
del *.class
cls