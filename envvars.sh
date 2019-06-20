# Base directory for AppDYnamics Java agent
# Configure this based on directory location of the Java Agent and its version
export APPD_JAVA_AGENT_DIR=/Users/david.ryder/Downloads/AppD-Downloads/AppServerAgent-4.5.4.24355

# AppDynamics javaagent.jar file
export APPDYNAMICS_APP_AGENT_JAR_FILE=$APPD_JAVA_AGENT_DIR/javaagent.jar

# Application Name, Tier and Node
export APPDYNAMICS_AGENT_APPLICATION_NAME="JAVA_TEST_1"
export APPDYNAMICS_AGENT_TIER_NAME="TIER_JT1"
export APPDYNAMICS_AGENT_NODE_NAME="NODE_JT1"

# Controller
export APPDYNAMICS_CONTROLLER_HOST_NAME=dryderc1-drydertest1-lbthj36l.srv.ravcloud.com
export APPDYNAMICS_CONTROLLER_PORT=8090
export APPDYNAMICS_CONTROLLER_SSL_ENABLED=false
export APPDYNAMICS_AGENT_ACCOUNT_NAME=customer1
export APPDYNAMICS_AGENT_ACCOUNT_ACCESS_KEY=5911238b-a3fa-4499-be7d-7786f615ecee
export APPDYNAMICS_GLOBAL_ACCOUNT_NAME=customer1_b9687dc4-a26d-462e-911a-3fc13c0f7dd4
export APPDYNAMICS_ANALYTICS_AGENT_URL=http://localhost:9090/v2/sinks/bt
export APPDYNAMICS_EVENTS_SERVICE_ENDPOINT="http://drydersys5apps-drydertest1-owsosu0r.srv.ravcloud.com:9080"

# Controller authentication
export APP_ACCOUNT=customer1
export APPD_CONTROLLER_ADMIN=admin
export APPD_UNIVERSAL_PWD=welcome1

# Machine Agent
export APPDYNAMICS_SIM_ENABLED=true
