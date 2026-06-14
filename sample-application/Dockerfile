FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/sample-application-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
