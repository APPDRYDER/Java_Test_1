<<<<<<< HEAD
#!/bin/bash
#
#

export JAVA_AGENT=/home/ddr/agents/app-4.5.17.28908/ver4.5.17.28908/javaagent.jar

#export JAVA_AGENT=/home/ddr/agents/app4.5.7.25056/ver4.5.15.28231/javaagent.jar

export APPDYNAMICS_AGENT_APPLICATION_NAME=DDR_JAVA_TEST_APP_1
export APPDYNAMICS_AGENT_NODE_NAME=NODE_JT1
export APPDYNAMICS_AGENT_TIER_NAME=TIER_JT1
=======
# Base directory for AppDYnamics Java agent
# Configure this based on directory location of the Java Agent and its version
export APPD_JAVA_AGENT_DIR=/Users/david.ryder/Downloads/AppD-Downloads/AppServerAgent-4.5.4.24355

# AppDynamics javaagent.jar file
export APPDYNAMICS_APP_AGENT_JAR_FILE=$APPD_JAVA_AGENT_DIR/javaagent.jar

>>>>>>> 8e99d2f0690a3e497a67c05053d43b2def142949
