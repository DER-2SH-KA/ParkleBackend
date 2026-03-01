FROM eclipse-temurin:21-jre-alpine-3.22
WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /app/app.jar
ENTRYPOINT ["java", "-Xms32m", "-Xmx100m",\
"-XX:MaxMetaspaceSize=140m", "-Xss512k", "-XX:ReservedCodeCacheSize=64m",\
"-XX:+UseSerialGC", "-jar", "/app/app.jar"]