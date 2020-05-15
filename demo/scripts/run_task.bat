::  java -cp ..\target\java-utils-v1.0.0.jar TestSingal

:: usage "./run_task.bat run_task.properties"
@echo off
"%JAVA_HOME%\bin\java" ^
    -cp ..\target\java-utils-v1.0.0.jar ^
    -Dlog4j.configurationFile=../config/log4j.properties
    %JVM_OPTS% TestSingal %*
