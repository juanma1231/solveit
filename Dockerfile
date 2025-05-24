# Multi-stage build for a more efficient Docker image

# Build stage
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew && ./gradlew bootJar

# Runtime stage
FROM eclipse-temurin:21-jre
WORKDIR /app

# Create a non-root user to run the application
RUN addgroup --system --gid 1001 appuser && \
    adduser --system --uid 1001 --gid 1001 appuser

# Copy the JAR file from the build stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Set ownership of the application files
RUN chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Expose the port the app runs on
EXPOSE 8080

# Set Spring profiles and other environment variables
ENV SPRING_PROFILES_ACTIVE=prod

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]