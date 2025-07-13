FROM openjdk:21-jre-slim

WORKDIR /app

COPY target/*.jar app.jar

RUN mkdir -p /app/logs

EXPOSE 8083

ENV JAVA_OPTS="-Xmx512m -Xms256m"

CMD ["java", "-jar", "app.jar"]