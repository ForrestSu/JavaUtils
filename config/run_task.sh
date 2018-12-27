#!/usr/bin/bash
## usage "sh ./run_task.sh run_task.properties"

${JAVA_HOME}/bin/java \
    -cp ../target/java-utils-v1.0.0.jar \
    ${JVM_OPTS} com.sunquan.ipc.TestSingal "$@"