::  java -cp ..\target\java-utils-v1.0.0.jar com.sunquan.ipc.TestSingal

:: usage "./run_task.bat run_task.properties"
@echo off
"%JAVA_HOME%\bin\java" ^
    -cp ..\target\java-utils-v1.0.0.jar ^
    %JVM_OPTS% com.sunquan.ipc.TestSingal %*
