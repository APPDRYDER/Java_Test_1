#!/bin/bash
#
#


# Compile
javac -cp $APPDYNAMICS_APP_AGENT_JAR_FILE  TestQueue1.java

# Compile the Java SDK TestApp1 Interceptor
javac -cp $APPDYNAMICS_APP_AGENT_JAR_FILE  TestApp1Interceptor1.java
