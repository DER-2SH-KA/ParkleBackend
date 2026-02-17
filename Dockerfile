FROM eclipse-temurin:21-jre-alpine-3.22
WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /app/app.jar
ENTRYPOINT ["java", "-Xms50m", "-XX:ms100m", "-XX:MaxMetaspaceSize=128m", "-XX:+UseSerialGC", "-jar", "/app/app.jar"]