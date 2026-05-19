# ============ Stage 1: Build ============
FROM maven:3.9.5-eclipse-temurin-17-alpine AS builder

WORKDIR /app

# Cache dependencies first
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Build application
COPY src ./src
RUN mvn clean package -DskipTests -B

# ============ Stage 2: Runtime ============
FROM eclipse-temurin:17-jre-alpine AS runtime

WORKDIR /app

# Create non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Create uploads directory
RUN mkdir -p uploads logs && chown -R appuser:appgroup /app

# Copy the jar from builder stage
COPY --from=builder /app/target/*.jar app.jar

USER appuser

EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# JVM optimizations
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC -XX:+HeapDumpOnOutOfMemoryError"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
