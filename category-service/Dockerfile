FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/*.jar app.jar

RUN mkdir -p /app/logs

EXPOSE 8082

ENV JAVA_OPTS="-Xmx512m -Xms256m"

CMD ["java", "-jar", "app.jar"]