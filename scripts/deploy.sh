#!/bin/bash

SPRING_PROFILE=$1

DEPLOY_PATH="/home/ec2-user/woowahan-delivery"
JAR_FILE=$(ls -tr "$DEPLOY_PATH"/*.jar 2>/dev/null | grep -v 'plain' | tail -n 1)

if [ -z "$JAR_FILE" ]; then
  echo "❌ JAR 파일을 찾을 수 없습니다: $DEPLOY_PATH"
  exit 1
fi

echo "🚀 배포할 JAR: $JAR_FILE"

cat > "$DEPLOY_PATH/start.sh" <<EOF
#!/bin/bash
exec java \\
  -XX:+UseG1GC \\
  -jar $JAR_FILE \\
  --spring.profiles.active=$SPRING_PROFILE \\
  --server.tomcat.threads.max=400 \\
  --spring.datasource.hikari.maximum-pool-size=25
EOF

chmod +x "$DEPLOY_PATH/start.sh"

echo "🔄 앱 재시작 중..."
sudo systemctl restart woowahan-delivery

echo "🏥 헬스체크 시작..."
for i in {1..12}; do
  sleep 5
  STATUS=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8081/actuator/health 2>/dev/null)

  if [ "$STATUS" = "200" ]; then
    echo "✅ 배포 완료 (${i}번째 시도)"
    exit 0
  fi

  echo "⏳ 대기 중... ($i/12) HTTP STATUS: $STATUS"
done

echo "❌ 헬스체크 실패 - 로그를 확인하세요"
echo "로그 확인: sudo journalctl -u woowahan-delivery -n 50"
exit 1
