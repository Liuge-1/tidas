#!/bin/bash
# =============================================
# TIDAS 培训管理系统 — 服务器部署启动脚本
# 放置路径: /opt/tidas/start.sh
# =============================================

# ---- 生产环境变量（部署时必须设置）----
export JASYPT_ENCRYPTOR_PASSWORD="tidas2024secret"   # Jasypt 加密密钥，请修改为更强的密码
export JWT_SECRET="k8F2xPqL9mR5vN7wY1cE3tA6bD0gH4jK8oP2sT5vX7yZ1cB3dF6hJ9lM2nQ4rS8uW0x"  # JWT签名密钥，生产请换成新的随机字符串
export MYSQL_URL="jdbc:mysql://localhost:3306/tidas?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai&ssl-mode=DISABLED"
export MYSQL_USERNAME="root"
export MYSQL_PASSWORD="ENC(h7iIhMhS3l6oKNKVTmCWApkqKRq252nhM/Rg3RoWPQdcfdr6Iwij54nnVnoSz6mI)"

# ---- JVM 参数 ----
JAVA_OPTS="-Xms256m -Xmx512m -Dfile.encoding=UTF-8"

# ---- 启动 ----
JAR_FILE="/opt/tidas/tidas-web-management-0.0.1-SNAPSHOT.jar"

echo "Starting TIDAS..."
nohup java ${JAVA_OPTS} -jar ${JAR_FILE} > /opt/tidas/app.log 2>&1 &
echo "PID: $!"
echo "Log: /opt/tidas/app.log"
