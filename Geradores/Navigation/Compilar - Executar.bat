@setlocal enableextensions enabledelayedexpansion
@echo on
cls
javac NavigationGen_2.java
@echo off
pause
cls
java NavigationGen_2 navigation_7.dat 9	9 1.0 0.1 0.1 0.9
pause
end local
cls
del *.class
cls