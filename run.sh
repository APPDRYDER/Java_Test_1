#!/bin/bash
#

if [[ ! -v JAVA_AGENT ]]; then
        echo "Running without envvar JAVA_AGENT set"
        java TestApp1
else
        echo "Running with envvar JAVA_AGENT set to [$JAVA_AGENT]"
        java -javaagent:$JAVA_AGENT TestApp1
fi
