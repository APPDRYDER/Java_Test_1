#!/bin/bash
#
# Example AppDynamics Remediation Script
# Copy to AppDynamics Machine Agent local-scripts directory
#

# Timestamp and log file names
APP_NAME="ENVVARS"
APP_PID="1111"
TS=`date +%Y%m%d-%H%M%S`
LOG_FILE=/tmp/$APP_NAME-$TS-$APP_PID.log
echo "Starting Remediation Action for $APP_NAME $LOG_FILE"

echo "Environment"                                    >> $LOG_FILE
pwd                                                   >> $LOG_FILE
env                                                   >> $LOG_FILE

echo "AppDynamics Remediation Environment Variables"   >> $LOG_FILE
echo "APP_ID [$APP_ID]"                                >> $LOG_FILE
echo "EVENT_TIME [$EVENT_TIME]"                        >> $LOG_FILE
echo "EVENT_ID [$EVENT_ID]"                            >> $LOG_FILE
echo "EVENT_TYPE [$EVENT_TYPE ]"                       >> $LOG_FILE
echo "ENV_STARTUP_ARGS [$ENV_STARTUP_ARGS]"            >> $LOG_FILE
echo "ENV_STARTUP_OPTIONS [$ENV_STARTUP_OPTIONS]"      >> $LOG_FILE
echo "ENV_SYSTEM_PROPERTIES [$ENV_SYSTEM_PROPERTIES]"  >> $LOG_FILE
echo "AFFECTED_ENTITY [$AFFECTED_ENTITY]"              >> $LOG_FILE

echo "End Remediation Action for $APP_NAME $LOG_FILE"
