#!/bin/bash
#
#

if [ ! -s "$APPDYNAMICS_APP_AGENT_JAR_FILE" ]; then
  echo "Error: AppDynamics Java Agent not found (envvar: APPDYNAMICS_APP_AGENT_JAR_FILE): ($APPDYNAMICS_APP_AGENT_JAR_FILE)"
  exit 0
fi

# Compile
javac -Xlint:deprecation -cp $APPDYNAMICS_APP_AGENT_JAR_FILE  TestQueue1.java

# Compile the Java SDK TestApp1 Interceptor
javac -Xlint:deprecation -cp $APPDYNAMICS_APP_AGENT_JAR_FILE  TestApp1Interceptor1.java
