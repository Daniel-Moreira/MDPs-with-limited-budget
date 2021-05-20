@setlocal enableextensions enabledelayedexpansion
@echo on
cls
javac RelaxedTriangleTireWorldGen.java
@echo off
pause
cls
java RelaxedTriangleTireWorldGen TriangleTireWorld_1.dat 5 1.0 0.1
pause
end local
cls
del *.class
cls