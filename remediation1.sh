#!/bin/bash
#
#
# Example AppDynamics Remediation Script
# Run JFR on a named Java Applcation
# Copy to AppDynamics Machine Agent local-scripts directory
#

# Find PID of Java Application, JFR Duration in seconds
JFR_APP_NAME="TestQueue1"
JFR_RECORDING_DURATION_SEC=30

# Find PID oF proess usig jcmd
APP_PID=`jcmd | grep $JFR_APP_NAME | cut -d' ' -f1`
APP_PID=${APP_PID:-"notfound"}

# Timestamp and log file names
TS=`date +%Y%m%d-%H%M%S`
JFR_FILE=/tmp/$JFR_APP_NAME-$TS-$APP_PID.jfr
LOG_FILE=/tmp/$JFR_APP_NAME-$TS-$APP_PID.log
echo "Starting Remediation Action for $JFR_APP_NAME PID[$APP_PID]"

echo "Environment"                                    >> $LOG_FILE
java -version                                         >> $LOG_FILE   2>&1
jcmd                                                  >> $LOG_FILE
pwd                                                   >> $LOG_FILE
env                                                   >> $LOG_FILE

echo "AppDynamics Remediation Environment Variables"   >> $LOG_FILE
echo "APP_ID [$APP_ID]"                                >> $LOG_FILE
echo "EVENT_TIME [$EVENT_TIME]"                        >> $LOG_FILE
echo "EVENT_ID [$EVENT_ID]"                            >> $LOG_FILE
echo "EVENT_TYPE [$EVENT_TYPE ]"                       >> $LOG_FILE
echo "ENV_STARTUP_OPTIONS [$ENV_STARTUP_OPTIONS]"      >> $LOG_FILE
echo "ENV_SYSTEM_PROPERTIES [$ENV_SYSTEM_PROPERTIES]"  >> $LOG_FILE
echo "AFFECTED_ENTITY [$AFFECTED_ENTITY]"              >> $LOG_FILE

# Start a JFR recording
jcmd $APP_PID JFR.start \
              settings=profile \
              duration="$JFR_RECORDING_DURATION_SEC"s \
              filename=$JFR_FILE  >> $LOG_FILE

echo "End Remediation Action for $JFR_APP_NAME PID[$APP_PID] $LOG_FILE"
