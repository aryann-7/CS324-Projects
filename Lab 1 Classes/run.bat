@echo off
setlocal EnableExtensions EnableDelayedExpansion 

javac music\*.java
java music.Music

pause