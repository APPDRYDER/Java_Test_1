#!/bin/bash
#
#
DURATION_SEC=${1:-"3600"}
WAIT_MS=${2:-"15000"}
NODE_SEQ=`echo ${3:-"1"} | sed 's/\,/ /g'` # Expects n or n,n,... with no spaces
JAVA_APPLICATION_NAME=${4:-"TestApp1"}

_validateEnvironmentVars() {
  echo "Validating environment variables for $1"
  shift 1
  VAR_LIST=("$@") # rebuild using all args
  #echo $VAR_LIST
  for i in "${VAR_LIST[@]}"; do
     [ -z ${!i} ] && { echo "Environment variable not set: $i"; ERROR="1"; }
  done
  [ "$ERROR" == "1" ] && { echo "Exiting"; exit 1; }
}

_validateEnvironmentVars "Run Server" \
  "APPDYNAMICS_AGENT_APPLICATION_NAME" "APPDYNAMICS_AGENT_TIER_NAME" "APPDYNAMICS_AGENT_NODE_NAME" \
  "APPDYNAMICS_APP_AGENT_JAR_FILE"

export JAVA_OPTS=""
export JAVA_OPTS=$JAVA_OPTS"-Dappdynamics.low.entropy=true "
export JAVA_OPTS=$JAVA_OPTS"-Dallow.unsigned.sdk.extension.jars=true "
export JAVA_OPTS=$JAVA_OPTS"-cp $APPDYNAMICS_APP_AGENT_JAR_FILE:."

# Network Visibility
#export LD_PRELOAD="/home/ddr/agent-net/lib/appd-netlib.so "
#export JAVA_OPTS=$JAVA_OPTS"-Dappdynamics.socket.collection.bci.enable=true "

if [ -e "$APPDYNAMICS_APP_AGENT_JAR_FILE" ]; then
  export JAVA_OPTS="$JAVA_OPTS -javaagent:$APPDYNAMICS_APP_AGENT_JAR_FILE"
else
  echo "AppDynamics Java Agent not found: $APPDYNAMICS_APP_AGENT_JAR_FILE"
  exit 0
fi

_startServer1() {
  NAME=$1
  ERROR_RATE=$2
  THREADS=1
  export APPDYNAMICS_AGENT_NODE_NAME=$NAME
  echo "Starting: $APPDYNAMICS_AGENT_NODE_NAME $JAVA_APPLICATION_NAME $JAVA_OPTS"
  nohup java $JAVA_OPTS $JAVA_APPLICATION_NAME $DURATION_SEC $WAIT_MS $ERROR_RATE $THREADS $NAME &
}

# Kill existing processes
echo "Stopping existing applications instances"
pkill -e -f "$JAVA_APPLICATION_NAME"

echo "Starting: Iterations: $ITERATIONS Wait: $WAIT_MS Nodes: $NODE_SEQ"
# Stop Previous
#pkill -9 -f "$APPDYNAMICS_AGENT_APPLICATION_NAME"

# Start N nodes
BASE_NODE_NAME=$APPDYNAMICS_AGENT_NODE_NAME
ERRROR_RATE="0.2"
for NODE_ID in $NODE_SEQ; do
  _startServer1 $BASE_NODE_NAME"_$NODE_ID_" "$ERRROR_RATE"
done


echo "Complete"
