# Multi-stage build: Stage 1 - Build with Maven
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /build

# Copy parent pom and modules from parent directory
COPY ../pom.xml .
COPY ../self-healing-starter/ ./self-healing-starter/
COPY . ./sample-application/

# Build the entire project with optimized Maven settings
RUN mvn clean install -DskipTests -q -B -V \
    -Dorg.slf4j.simpleLogger.defaultLogLevel=info \
    -Dmaven.artifact.threads=10 \
    && echo "✓ Maven build completed"

# Stage 2 - Runtime with just JRE
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the built JAR from builder stage
COPY --from=builder /build/sample-application/target/sample-application-1.0.0.jar app.jar

EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD java -cp app.jar -Dspring.boot.project.name=app health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]

