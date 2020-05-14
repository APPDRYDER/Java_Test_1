#!/bin/bash
#
#
SECONDS_DAY=86440
SECONDS_WEEK=604800
DURATION_SEC=${1:-"$SECONDS_DAY"}
WAIT_MS=${2:-"5000"}
NODE_SEQ=`echo ${3:-"1"} | sed 's/\,/ /g'` # Expects n or n,n,... with no spaces
JAVA_APPLICATION_NAME=${4:-"TestQueue1"}
ITERATIONS=1

# Bash Utility Functions
. bash-functions.sh

JAVA_VER=`java -version 2>&1 | awk -F '"' '/version/ {print $2}'`
JAVA_TYPE=`java -version 2>&1 | awk -F ' ' '/Java\(TM\)/ {print $1}'`

_validateEnvironmentVars "Run Server" \
  "APPDYNAMICS_AGENT_APPLICATION_NAME" "APPDYNAMICS_AGENT_TIER_NAME" "APPDYNAMICS_AGENT_NODE_NAME" \
  "APPDYNAMICS_APP_AGENT_JAR_FILE"

export JAVA_OPTS=""
if [ -s "$APPDYNAMICS_APP_AGENT_JAR_FILE" ]; then
  export JAVA_OPTS="$JAVA_OPTS -javaagent:$APPDYNAMICS_APP_AGENT_JAR_FILE "
else
  echo "Error: AppDynamics Java Agent not found (APPDYNAMICS_APP_AGENT_JAR_FILE): $APPDYNAMICS_APP_AGENT_JAR_FILE"
  exit 0
fi
export JAVA_OPTS=$JAVA_OPTS"-Dappdynamics.low.entropy=true "
export JAVA_OPTS=$JAVA_OPTS"-Dallow.unsigned.sdk.extension.jars=true "
export JAVA_OPTS=$JAVA_OPTS"-Dappdynamics.analytics.agent.url=$APPDYNAMICS_ANALYTICS_AGENT_URL "
export JAVA_OPTS=$JAVA_OPTS"-classpath $APPDYNAMICS_APP_AGENT_JAR_FILE:. "

echo "JAVA_OPS [$JAVA_OPTS]"
# Requires: Java(TM) SE Runtime Environment 18.9 (build 11.0.3+12-LTS)
#export JAVA_OPTS=$JAVA_OPTS"-XX:+UnlockCommercialFeatures -XX:+FlightRecorder "

# Network Visibility
#export LD_PRELOAD="/home/ddr/agent-net/lib/appd-netlib.so "
#export JAVA_OPTS=$JAVA_OPTS"-Dappdynamics.socket.collection.bci.enable=true "



_startServer1() {
  NAME=$1
  ERROR_RATE=$2
  THREADS=3
  export APPDYNAMICS_AGENT_NODE_NAME=$NAME
  echo "Starting: [$JAVA_OPTS] $JAVA_APPLICATION_NAME $APPDYNAMICS_AGENT_NODE_NAME "
  nohup java $JAVA_OPTS $JAVA_APPLICATION_NAME $DURATION_SEC $WAIT_MS $ERROR_RATE $THREADS $NAME &
}

# Kill existing processes
echo "Stopping existing applications instances"
pkill -f "$JAVA_APPLICATION_NAME"
sleep 2
echo "Starting: Iterations: $ITERATIONS Wait: $WAIT_MS Nodes: $NODE_SEQ"

# Start N nodes
rm -rf nohup.out
sleep 1
BASE_NODE_NAME=$APPDYNAMICS_AGENT_NODE_NAME
ERRROR_RATE="20"
for NODE_ID in $NODE_SEQ; do
  _startServer1 $BASE_NODE_NAME"_$NODE_ID" "$ERRROR_RATE"
done

# Tail log
sleep 2
_tailLog 300 nohup.out

echo ""
echo "Complete"
