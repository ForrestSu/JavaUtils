#!/usr/bin/bash
## usage "sh ./run_task.sh run_task.properties"

kill $(ps aux | grep java | grep $1 | grep -v grep | awk '{print $2}')

# ps -ef |grep java |grep $1 |grep -v grep | awk '{print $2}' |xargs kill


# $(ps aux | grep java | grep -v grep | awk '{print $2}')