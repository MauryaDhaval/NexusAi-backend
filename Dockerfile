# ==============================================================================
# Stage 1: Build Stage (Downloads dependencies and compiles code)
# ==============================================================================
FROM maven:3.8.5-openjdk-17-slim AS builder
WORKDIR /app

# Copy pom.xml first to leverage Docker layer caching for Maven dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the source code and compile the application jar skipping tests
COPY src ./src
RUN mvn clean package -DskipTests

# ==============================================================================
# Stage 2: Runtime Stage (Lightweight JRE runner)
# ==============================================================================
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the built jar from the builder stage
COPY --from=builder /app/target/nexusai-0.0.1-SNAPSHOT.jar app.jar

# Expose the API port configured in application.properties
EXPOSE 8081

# Command to execute the application
ENTRYPOINT ["java", "-jar", "app.jar"]
