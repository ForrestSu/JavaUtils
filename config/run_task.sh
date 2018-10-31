#!/usr/bin/bash
## usage "sh ./run_task.sh run_task.properties"

${JAVA_HOME}/bin/java \
    -cp ../target/sftptask-1.0.jar \
    ${JVM_OPTS} com.xuncetech.sftp.TimerDownload "$@"