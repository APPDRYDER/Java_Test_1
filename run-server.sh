#!/bin/bash
#
#
ITERATIONS=${1:-"2"}
WAIT_MS=${2:-"15000"}
NODE_SEQ=`echo ${3:-"1"} | sed 's/\,/ /g'` # Expects n or n,n,... with no spaces

APPLICATION_NAME=TestApp1

# Agent config
export APPDYNAMICS_AGENT_NODE_NAME=NODE_JT1
export APPDYNAMICS_AGENT_TIER_NAME=TIER_JT1

export JAVA_OPTS=""
export JAVA_OPTS=$JAVA_OPTS"-Dappdynamics.low.entropy=true "
export JAVA_OPTS=$JAVA_OPTS"-Dallow.unsigned.sdk.extension.jars=true "
export JAVA_OPTS=$JAVA_OPTS"-cp /Users/david.ryder/Downloads/AppD-Downloads/AppServerAgent-4.5.4.24355/ver4.5.4.24355/javaagent.jar:."

# Network Visibility
#export LD_PRELOAD="/home/ddr/agent-net/lib/appd-netlib.so "
#export JAVA_OPTS=$JAVA_OPTS"-Dappdynamics.socket.collection.bci.enable=true "

if [ -e "$APPDYNAMICS_APP_AGENT_JAR_FILE" ]; then
  export JAVA_OPTS="$JAVA_OPTS -javaagent:$APPDYNAMICS_APP_AGENT_JAR_FILE"
else
  echo "AppDynamics Java Agent not found: $APPDYNAMICS_APP_AGENT_JAR_FILE"
  exit 0
fi

SERVER_PID_FILE="/TMP/TestApp1-test-server1.PID"
LOAD_PID_FILE="/TMP/TestApp1-load1.PID"

_startServer1() {
  NAME=$1
  export APPDYNAMICS_AGENT_NODE_NAME=$NAME
  TS=`date +%Y%m%d-%H%M%S`
  PID_FILE="/TMP/jetty-test-server1.PID"
  echo "Starting: $APPDYNAMICS_AGENT_NODE_NAME $JAVA_OPTS"
  nohup java $JAVA_OPTS $APPLICATION_NAME $ITERATIONS $WAIT_MS $NAME &

  #echo $! > $SERVER_PID_FILE
  #cat $SERVER_PID_FILE
}

echo "Starting: Iterations: $ITERATIONS Wait: $WAIT_MS Nodes: $NODE_SEQ"
# Stop Previous
#pkill -9 -f "$APPLICATION_NAME"

# Start N nodes
BASE_NODE_NAME=$APPDYNAMICS_AGENT_NODE_NAME
for NODE_ID in $NODE_SEQ; do
  _startServer1 $BASE_NODE_NAME"_$NODE_ID"
done


echo "Complete"
