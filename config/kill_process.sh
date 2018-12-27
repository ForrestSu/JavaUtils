#!/usr/bin/bash
## usage "sh ./run_task.sh run_task.properties"

kill -SIGINT $(ps aux | grep ProgrammName | grep -v grep | awk '{print $2}')

