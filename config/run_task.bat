:: usage "./run_task.bat run_task.properties"
@echo off
"%JAVA_HOME%\bin\java" ^
    -cp ..\target\sftptask-1.0.jar ^
    %JVM_OPTS% com.xuncetech.sftp.TimerDownload %*