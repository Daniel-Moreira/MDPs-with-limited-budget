@setlocal enableextensions enabledelayedexpansion
@echo on
cls
javac SSPSysAdminUniRingGen.java
@echo off
pause
cls
java SSPSysAdminUniRingGen SysAdmin_10.dat 15 1.0 0.1
pause
end local
cls
del *.class
cls