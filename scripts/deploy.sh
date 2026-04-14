#!/bin/bash

SERVER_IP=$1
SPRING_PROFILE=$2

DEPLOY_PATH="/home/ec2-user/woowahan-delivery"
CONFIG_PATH="$DEPLOY_PATH/config/application-${SPRING_PROFILE}.yml"
JAR_FILE=$(ls -tr "$DEPLOY_PATH"/*.jar 2>/dev/null | grep -v 'plain' | tail -n 1)

CURRENT_PID=$(pgrep -f "woowahan-delivery.*\.jar")
if [ -n "$CURRENT_PID" ]; then
  kill -15 "$CURRENT_PID"
  sleep 5
fi

nohup java -jar "$JAR_FILE" \
  --spring.profiles.active="$SPRING_PROFILE" \
  > "$DEPLOY_PATH/app.log" 2>&1 &

echo "배포 완료 → tail -f $DEPLOY_PATH/app.log"

